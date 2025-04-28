package com.goorm.thelastsupper.reservation.history.entity;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.goorm.thelastsupper.common.entity.BaseEntity;
import com.goorm.thelastsupper.account.entity.Account;
import com.goorm.thelastsupper.reservation.slot.entity.ReservationSlot;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "reservation_history")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
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
    //다른곳에서 사용시 메서드 수정 필요
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

}
