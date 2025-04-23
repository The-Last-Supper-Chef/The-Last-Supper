package com.goorm.thelastsupper.reservation.entity;

import com.goorm.thelastsupper.common.entity.BaseEntity;
import com.goorm.thelastsupper.account.entity.Account;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "reservation_history")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ReservationHistory extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "account_id",
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT)
    )
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

    private Long reservedPeople;

    @Column(name = "is_visible")
    private boolean visible;
}
