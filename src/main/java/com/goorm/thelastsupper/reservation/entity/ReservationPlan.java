package com.goorm.thelastsupper.reservation.entity;

import com.goorm.thelastsupper.common.entity.BaseEntity;
import com.goorm.thelastsupper.restaurant.entity.Restaurant;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "reservation_plan")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ReservationPlan extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "restaurant_id",
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT)
    )
    private Restaurant restaurant;

    private LocalDate startDate;

    private LocalDate endDate;

    @ElementCollection(targetClass = DayOfWeek.class)
    @CollectionTable(
            name = "weekdays",
            joinColumns = @JoinColumn(name = "plan_id")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "weekday")
    private Set<DayOfWeek> weekdays = new HashSet<>();

    private LocalTime openTime;

    private LocalTime closeTime;

    private LocalTime breakOpenTime;

    private LocalTime breakCloseTime;

    private Long turnTime;
}
