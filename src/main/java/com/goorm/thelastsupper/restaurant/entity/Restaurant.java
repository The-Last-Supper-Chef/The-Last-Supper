package com.goorm.thelastsupper.restaurant.entity;

import com.goorm.thelastsupper.common.entity.BaseEntity;
import com.goorm.thelastsupper.account.entity.Account;
import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "restaurant")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Restaurant extends BaseEntity {

    private String restaurantName;

    private String restaurantLocation;

    private Industry industry;

    private String restaurantNumber;

    private String introduction;

    private LocalTime openTime;

    private LocalTime closeTime;

    @ElementCollection(targetClass = DayOfWeek.class)
    @CollectionTable(
            name = "weekdays",
            joinColumns = @JoinColumn(name = "plan_id")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "weekday")
    private Set<DayOfWeek> weekdays = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "account_id",
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT)
    )
    private Account account;
}
