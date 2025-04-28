package com.goorm.thelastsupper.waiting.dto;

import com.goorm.thelastsupper.waiting.entity.WaitingSetting;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record WaitingSettingResponse(
        String restaurantId,
        @NotNull
        String category, // ex: "열림"
        LocalDateTime updateAt
) {

    public static WaitingSettingResponse from(WaitingSetting setting) {
        return new WaitingSettingResponse(
                setting.getRestaurant().getId(),
                setting.getWaitingSetCategory().getName(),
                setting.getUpdatedAt()
        );
    }
}
