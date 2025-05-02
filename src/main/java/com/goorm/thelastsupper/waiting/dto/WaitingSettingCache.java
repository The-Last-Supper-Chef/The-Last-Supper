package com.goorm.thelastsupper.waiting.dto;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.goorm.thelastsupper.restaurant.entity.Restaurant;
import com.goorm.thelastsupper.waiting.entity.WaitingSetCategory;
import com.goorm.thelastsupper.waiting.entity.WaitingSetting;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonSerialize
public class WaitingSettingCache {
    private  String restaurantId;
    private  WaitingSetCategory category;
    private  LocalDateTime createdAt;
    private  LocalDateTime updatedAt;

    public static WaitingSettingCache from(WaitingSetting setting) {
        return new WaitingSettingCache(
                setting.getRestaurant().getId(),
                setting.getWaitingSetCategory(),
                setting.getCreatedAt(),
                setting.getUpdatedAt()
        );
    }

    public WaitingSetting toEntity(Restaurant restaurant) {
        return WaitingSetting.builder()
                .restaurant(restaurant)
                .waitingSetCategory(category)
                .build();
    }
}

