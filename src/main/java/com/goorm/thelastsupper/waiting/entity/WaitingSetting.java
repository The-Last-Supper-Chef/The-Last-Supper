package com.goorm.thelastsupper.waiting.entity;

import com.goorm.thelastsupper.common.entity.BaseEntity;
import com.goorm.thelastsupper.restaurant.entity.Restaurant;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Table(name = "waiting_setting")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class WaitingSetting extends BaseEntity {
    // 웨이팅 세팅: 오픈, 차단, 마감
    @Enumerated(EnumType.STRING)
    private WaitingSetCategory waitingSetCategory;

    // 매장 정보
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "restaurant_id",
            nullable = false,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT)
    )
    private Restaurant restaurant;

}
