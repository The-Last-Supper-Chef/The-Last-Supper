package com.goorm.thelastsupper.waiting.dto;

import com.goorm.thelastsupper.waiting.entity.WaitingHistory;
import com.goorm.thelastsupper.waiting.entity.WaitingQueue;
import com.goorm.thelastsupper.waiting.entity.WaitingStatus;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record WaitingQueueResponse(
        String waitingQueueId,
        String accountId,
        WaitingStatus waitingStatus,
        int headCount,
        Long number,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String createdBy,
        String updatedBy) {
    public static WaitingQueueResponse toWaitingQueueResponse (WaitingQueue waitingQueue) {
        return WaitingQueueResponse.builder()
                .waitingQueueId(waitingQueue.getId())
                .accountId(waitingQueue.getAccount().getId())
                .waitingStatus(waitingQueue.getWaitingStatus())
                .headCount(waitingQueue.getHeadCount())
                .number(waitingQueue.getNumber())
                .createdAt(waitingQueue.getCreatedAt())
                .updatedAt(waitingQueue.getUpdatedAt())
                .createdBy(waitingQueue.getCreatedBy())
                .updatedBy(waitingQueue.getUpdatedBy())
                .build();
    }
}
