package com.goorm.thelastsupper.reservation.entity;

import com.goorm.thelastsupper.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;
import java.time.LocalDate;

@Entity
@Table(name = "reservation_slot")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class ReservationSlot extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "plan_id",
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT)
    )
    private ReservationPlan plan;

    private LocalDate date;

    private LocalTime startTime;

    private int capacityTotal;  // 총 인원

    private int remaining;      // 잔여 인원

    @Enumerated(EnumType.STRING)
    private SlotStatus state;
}
