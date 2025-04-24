package com.goorm.thelastsupper.waiting.dto;

import com.goorm.thelastsupper.waiting.entity.WaitingQueue;
import com.goorm.thelastsupper.waiting.entity.WaitingStatus;
import lombok.Builder;

@Builder
public record WaitingResponse(
        String waitingQueueId,
        String accountId,
        WaitingStatus waitingStatus,
        int headCount,
        Long number
) {
    public static WaitingResponse toWaitingResponse (WaitingQueue waitingQueue) {
        return WaitingResponse.builder()
                .waitingQueueId(waitingQueue.getId())
                .accountId(waitingQueue.getAccount().getId())
                .waitingStatus(waitingQueue.getWaitingStatus())
                .headCount(waitingQueue.getHeadCount())
                .number(waitingQueue.getNumber())
                .build();
    }
}
