package com.goorm.thelastsupper.reservation.history.entity;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.goorm.thelastsupper.common.entity.BaseEntity;
import com.goorm.thelastsupper.account.entity.Account;
import com.goorm.thelastsupper.reservation.common.error.ReservationErrorCode;
import com.goorm.thelastsupper.reservation.common.exception.ReservationException;
import com.goorm.thelastsupper.reservation.history.dto.ReservationResponse;
import com.goorm.thelastsupper.reservation.plan.dto.OpenSlotsCommandRequest;
import com.goorm.thelastsupper.reservation.plan.entity.ReservationPlan;
import com.goorm.thelastsupper.reservation.slot.entity.ReservationSlot;
import com.goorm.thelastsupper.restaurant.entity.Restaurant;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Getter // FIXME: 현재 예약 알림 도메인에서 사용되고 있음.
@Slf4j
@Entity
@Table(name = "reservation_history")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationHistory extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "account_id",
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT)
    )
    @NotFound(action = NotFoundAction.IGNORE)
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "reservation_slot_id",
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT)
    )
    private ReservationSlot reservationSlot;

    private String request;

    @Enumerated(EnumType.STRING)
    private ReservedStatus reservedStatus;

    @Enumerated(EnumType.STRING)
    private RejectionReason rejectionReason;

    private int reservedPeople;

    @Column(name = "is_visible")
    private boolean visible;

    //registerReservation에서 history 저장하는 용도
    public static ReservationHistory createReservation(Account account, ReservationSlot slot, String request, int totalVisitors) {
        return ReservationHistory.builder()
                .account(account)
                .reservationSlot(slot)
                .request(request)
                .reservedStatus(ReservedStatus.CONFIRMED)
                .reservedPeople(totalVisitors)
                .visible(true)
                .build();
    }

    public void setCancel() {
        this.reservedStatus = ReservedStatus.CANCELED;
    }


    /** 이 히스토리가 이 계정의 것이 아니면 예외 */
    public void ensureOwnedBy(String accountId) {
        if (!this.account.getId().equals(accountId)) {
            throw new ReservationException(ReservationErrorCode.RESERVATION_ID_MISMATCH);
        }
    }

    /** 이 예약이 “확정” 상태가 아니면 예외 */
    public void ensureConfirmed() {
        log.info("예약 상태 확인: 예약 상태 = {}", this.reservedStatus);
        if (this.reservedStatus != ReservedStatus.CONFIRMED) {
            log.warn("예약 상태가 확정되지 않음: 예약 상태 = {}", this.reservedStatus);
            throw new ReservationException(ReservationErrorCode.RESERVATION_NOT_FOUND);
        }
    }

    /** 예약 상태가 확정(CONFIRMED)인지 */
    public boolean isConfirmed() {
        log.info("예약 상태 확인: 예약 상태 = {}", this.reservedStatus);
        return this.reservedStatus == ReservedStatus.CONFIRMED;
    }

    /** 이 히스토리가 특정 계정의 것인지 */
    public boolean isOwnedBy(String accountId) {
        return this.account.getId().equals(accountId);
    }

    /** 현재 몇명 예약 신청이 들어왔는 지 확인 */
    public int isReservedPeople() {
        return this.reservedPeople;
    }


    /**
     * 이 히스토리 엔티티를 응답 DTO로 변환합니다.
     */
    public ReservationResponse toResponse() {
        return new ReservationResponse(
            this.account.getId(),
            this.reservationSlot.getId(),
            this.request,
            this.reservedStatus,
            this.rejectionReason,
            this.reservedPeople,
            this.visible
        );
    }

}
