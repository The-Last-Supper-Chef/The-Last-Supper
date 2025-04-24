package com.goorm.thelastsupper.waiting.dto;

import com.goorm.thelastsupper.restaurant.entity.Restaurant;
import com.goorm.thelastsupper.waiting.entity.WaitingSetCategory;
import com.goorm.thelastsupper.waiting.entity.WaitingSetting;
import com.goorm.thelastsupper.waiting.exception.WaitingErrorCode;
import com.goorm.thelastsupper.waiting.exception.WaitingException;

public record WaitingSettingRequest(String restaurantId, WaitingSetCategory category) {


    public WaitingSettingRequest {
        if (restaurantId == null) {
            throw new WaitingException(WaitingErrorCode.RESTAURANT_ID_REQUIRED);
        }
    }

        public WaitingSetting toEntity(Restaurant restaurant) {
            return WaitingSetting.builder()
                    .restaurant(restaurant)
                    .waitingSetCategory(category)
                    .build();
        }




    }
