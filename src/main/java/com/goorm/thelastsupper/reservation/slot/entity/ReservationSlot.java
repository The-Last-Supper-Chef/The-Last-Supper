package com.goorm.thelastsupper.reservation.slot.entity;

import com.goorm.thelastsupper.common.entity.BaseEntity;
import com.goorm.thelastsupper.reservation.common.error.ReservationErrorCode;
import com.goorm.thelastsupper.reservation.common.exception.ReservationException;
import com.goorm.thelastsupper.reservation.plan.entity.SlotStatus;
import com.goorm.thelastsupper.reservation.plan.entity.ReservationPlan;
import com.goorm.thelastsupper.reservation.slot.dto.ReservationSlotDTO;
import com.goorm.thelastsupper.reservation.slot.dto.ReservationSlotResponse;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalTime;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
@Entity
@Table(name = "reservation_slot")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReservationSlot extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id",
        foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private ReservationPlan plan;

    private LocalDate date;
    private LocalTime startTime;

    /** 날짜 + 시각 합성 (UNIQUE) */
    @Column(name = "slot_date_time", nullable = false, unique = true)
    private LocalDateTime slotDateTime;

    private int       capacityTotal;
    private int       remaining;

    @Enumerated(EnumType.STRING)
    private SlotStatus status;

    /* -------- 생성 팩터리 -------- */
    public static ReservationSlot of(ReservationPlan plan,
        LocalDate date,
        LocalTime startTime,
        SlotStatus status,
        int capacity) {

        ReservationSlot s = new ReservationSlot();
        s.plan          = plan;
        s.date          = date;
        s.startTime     = startTime;
        s.slotDateTime  = LocalDateTime.of(date, startTime);
        s.capacityTotal = capacity;
        s.remaining     = capacity;
        s.status        = status;
        return s;
    }

    public void decreaseRemaining(int totalVisitors){
        remaining -= totalVisitors;
    }
    public void increaseRemaining(int totalVisitors){
        remaining += totalVisitors;
    }

    public void setHold() {
        this.status = SlotStatus.HOLD;
    }
    public void setOpen() {
        this.status = SlotStatus.OPEN;
    }
    public void setBlock() {
        this.status = SlotStatus.BLOCK;
    }

    /**
     * 이 슬롯의 수용 인원, 남은 인원, 상태를 한 번에 갱신합니다.
     *
     * @param capacityTotal 새로 설정할 총 수용 인원
     * @param remaining     새로 설정할 남은 예약 가능 인원
     * @param status        새로 설정할 슬롯 상태
     */
    public void updateCapacityAndStatus(int capacityTotal, int remaining, SlotStatus status) {
        this.capacityTotal = capacityTotal;
        this.remaining     = remaining;
        this.status        = status;
    }


    /**
     * 이 슬롯 엔티티를 응답 DTO로 변환합니다.
     */
    public ReservationSlotResponse toResponse() {
        return new ReservationSlotResponse(
            this.getId(),
            this.plan.getId(),
            this.date,
            this.startTime,
            this.capacityTotal,
            this.remaining,
            this.status
        );
    }

    /**
     * DTO 변환 메서드: 엔티티 → ReservationSlotDTO
     */
    public ReservationSlotDTO toDTO() {
        return new ReservationSlotDTO(
            this.getId(),
            this.plan.getId(),
            this.date,
            this.startTime,
            this.capacityTotal,
            this.remaining,
            this.status
        );
    }


    /** 열려있지 않은 상태인지 */
    public boolean isNotOpen() {
        return this.status.isNotOpen();
    }

    /** 이 슬롯이 아직 예약 가능한지? (OPEN 상태이고 남은 인원이 충분할 때) */
    public boolean isBookable(int requested) {
        return this.status == SlotStatus.OPEN && this.remaining >= requested;
    }

    /** 이 슬롯이 오픈 상태가 아니라면 예외를 던진다. */
    public void ensureOpen() {
        log.info("슬롯 오픈 상태 확인: slotId={}, status={}", this.getId(), this.status);
        if (status.isNotOpen()) {
            throw new ReservationException(ReservationErrorCode.RESERVATION_SLOT_CLOSED);
        }
    }

    /**
     * 총 예약 인원을 차감하며, 0이 되면 HOLD 상태로 전환.
     */
    public void reserve(int totalVisitors) {
        int remainingAfter = this.remaining - totalVisitors;
        if (remainingAfter < 0) {
            log.warn("예약 인원 초과: slotId={}, 요청 인원={}, 남은 인원={}",
                getId(), totalVisitors, this.remaining);
            throw new ReservationException(ReservationErrorCode.RESERVATION_CAPACITY_OVER);
        }
        if (remainingAfter == 0) {
            this.remaining = 0;
            this.status = SlotStatus.HOLD;
            log.info("잔여 인원 0 → HOLD 상태로 변경됨: slotId={}", getId());
        } else {
            this.remaining = remainingAfter;
            log.info("예약 가능: slotId={}, 요청 인원={}, 남은 인원={}",
                getId(), totalVisitors, this.remaining);
        }
    }


    /** 취소 가능 여부를 검사: 예약일 하루 전까지만 가능 */
    public void ensureCancellable() {
        if (LocalDate.now().isAfter(this.date.minusDays(1))) {
            log.warn("당일 예약, 취소 불가 조건 위반: 오늘={}, 예약일={}", LocalDate.now(), date);
            throw new ReservationException(ReservationErrorCode.RESERVATION_TIME_EXPIRED);
        }
        log.info("예약일 유효: 오늘={}, 예약일={}", LocalDate.now(), date);
    }

    /**
     * 취소 혹은 재증원 시, remaining 증가 후 HOLD 상태 해제 로직
     */
    public void restoreCapacity(int reservatedPeople) {
        // 1) 남은 인원 증가
        this.remaining += reservatedPeople;

        // 2) HOLD 상태였고, 남은 인원이 0 초과라면 OPEN으로 전환
        if (this.remaining > 0 && this.status == SlotStatus.HOLD) {
            this.status = SlotStatus.OPEN;
        }

        // 3) 로그
        log.info("잔여 인원 증가 완료. slotId={}, 남은 인원={}, slot 상태={}",
            this.getId(), this.remaining, this.status);
    }

}
