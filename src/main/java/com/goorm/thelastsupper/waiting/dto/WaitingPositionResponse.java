package com.goorm.thelastsupper.waiting.dto;

import lombok.Builder;

@Builder
public record WaitingPositionResponse(
        int position
) {
    public static WaitingPositionResponse toWaitingPositionResponse (int ahead) {
        return WaitingPositionResponse.builder()
                .position(ahead)
                .build();
    }
}
