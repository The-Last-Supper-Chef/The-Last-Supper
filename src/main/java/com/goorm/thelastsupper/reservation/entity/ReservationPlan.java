package com.goorm.thelastsupper.reservation.entity;

import com.goorm.thelastsupper.common.entity.BaseEntity;
import com.goorm.thelastsupper.reservation.dto.OpenSlotsCommand;
import com.goorm.thelastsupper.restaurant.entity.Restaurant;
import jakarta.persistence.*;
import lombok.*;

import java.time.*;
import java.util.*;

// TODO: 예약 계획 데이터 생성 시, 하루 씩 나눠서 저장시키기
/**
 * 예약 계획 엔티티 클래스
 * - 예약 슬롯을 관리하는 계획을 나타냅니다.
 */
@Entity
@Table(name = "reservation_plan")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReservationPlan extends BaseEntity {

    /* -------- 필드 -------- */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id",
        foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Restaurant restaurant;

    private LocalDate  startDate;
    private LocalDate  endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "weekday")
    private DayOfWeek weekday;

    private LocalTime  openTime;
    private LocalTime  closeTime;
    private LocalTime  breakOpenTime;
    private LocalTime  breakCloseTime;
    private long       turnTimeMinutes;

    /* JPA 보호용 생성자 */
    private ReservationPlan(Restaurant restaurant,
        LocalDate startDate, LocalDate endDate,
        DayOfWeek weekdays,
        LocalTime openTime, LocalTime closeTime,
        LocalTime breakOpenTime, LocalTime breakCloseTime,
        long turnTimeMinutes) {
        this.restaurant       = restaurant;
        this.startDate        = startDate;
        this.endDate          = endDate;
        this.weekday          = weekdays;
        this.openTime         = openTime;
        this.closeTime        = closeTime;
        this.breakOpenTime    = breakOpenTime;
        this.breakCloseTime   = breakCloseTime;
        this.turnTimeMinutes  = turnTimeMinutes;
    }

    /* -------- 팩터리 메소드 -------- */
    /**
     * 예약 계획을 생성하는 팩터리 메소드
     */
    public static ReservationPlan of(Restaurant restaurant,
        OpenSlotsCommand c) {
        return new ReservationPlan(
            restaurant,
            c.startDate(),
            c.endDate(),
            c.startDate().getDayOfWeek(),
            c.openTime(),
            c.closeTime(),
            c.breakTimes().isEmpty() ? null : c.breakTimes().get(0).start(),
            c.breakTimes().isEmpty() ? null : c.breakTimes().get(0).end(),
            c.slotDuration()
        );
    }

    /* -------- 슬롯 자동 생성 -------- */
    /**
     * 예약 슬롯을 자동으로 생성하는 메소드
     * - 예약 가능한 시간대에 맞춰 예약 슬롯을 생성하고, 브레이크 타임은 슬롯의 상태를 "블락"으로 변경
     *
     * @param cmd 예약 슬롯 생성 명령 객체
     * @return 예약 슬롯 리스트
     */
    public List<ReservationSlot> createSlots(OpenSlotsCommand cmd) {
        List<ReservationSlot> list = new ArrayList<>();
        LocalDate d = startDate;  // 시작 날짜부터 반복

        // 예약 기간 동안 반복
        while (!d.isAfter(endDate)) {

            // `dayOfWeekBased`가 true일 경우, 요일에 해당하는 날짜만 처리
            if (cmd.dayOfWeekBased() && !cmd.repeatDays().contains(weekday)) {
                d = d.plusDays(1);
                continue;
            }

            // `exceptionDates`에 포함된 날짜를 제외
            if (cmd.isExceptionDateEnabled() && cmd.exceptionDates() != null && cmd.exceptionDates().contains(d)) {
                d = d.plusDays(1);
                continue;  // 예외 날짜는 슬롯을 생성하지 않음
            }

            // 예약 슬롯 생성 로직을 별도의 메서드로 분리
            createSlotsForDate(d, cmd.capacity(), list);
            d = d.plusDays(1);  // 다음 날짜로 이동
        }
        return List.copyOf(list);  // 생성된 슬롯 리스트 반환
    }

    /**
     * 특정 날짜에 대해 예약 슬롯을 생성하는 메소드
     * - 해당 날짜의 시작 시간부터 종료 시간까지 예약 슬롯을 생성하고, 브레이크 타임을 고려하여 상태를 "블락"으로 설정
     *
     * @param date 예약 날짜
     * @param capacity 슬롯의 수용 가능 인원
     * @param slotList 생성된 예약 슬롯을 저장할 리스트
     */
    private void createSlotsForDate(LocalDate date, int capacity, List<ReservationSlot> slotList) {
        LocalDateTime cursor = LocalDateTime.of(date, openTime);  // 예약 시작 시간 설정

        // 종료 시간까지 반복하여 슬롯 생성
        while (!cursor.plusMinutes(turnTimeMinutes).toLocalTime()
            .isAfter(closeTime)) {

            LocalDateTime next = cursor.plusMinutes(turnTimeMinutes);  // 다음 슬롯의 시작 시간

            // 브레이크 타임에 포함되는지 여부를 판단
            boolean inBreak = isInBreakTime(cursor, next);

            // 브레이크 타임이면 슬롯을 "블락" 상태로 변경
            if (inBreak) {
                slotList.add(ReservationSlot.of(
                    this, date, cursor.toLocalTime(), SlotStatus.BLOCK, capacity));
            } else {
                // 일반 슬롯 생성
                slotList.add(ReservationSlot.of(
                    this, date, cursor.toLocalTime(), SlotStatus.OPEN, capacity));
            }
            cursor = next;  // 다음 슬롯의 시작 시간으로 이동
        }
    }

    /**
     * 주어진 시간 범위가 브레이크 시간에 포함되는지 확인하는 메소드
     *
     * @param cursor 현재 슬롯 시작 시간
     * @param next   다음 슬롯 시작 시간
     * @return 브레이크 시간에 포함되면 true, 아니면 false
     */
    private boolean isInBreakTime(LocalDateTime cursor, LocalDateTime next) {
        return breakOpenTime != null &&
            !(next.toLocalTime().isBefore(breakOpenTime.plusMinutes(turnTimeMinutes))  // 브레이크 시간 이전
                || cursor.toLocalTime().isAfter(breakCloseTime.minusMinutes(turnTimeMinutes)));  // 브레이크 시간 이후
    }


    /* -------- 예약 계획에 날짜 설정하는 메서드 -------- */
    public void setPlanDates(LocalDate date) {
        this.startDate = date;
        this.endDate = date;
        this.weekday = date.getDayOfWeek();
    }

}
