package com.goorm.thelastsupper.waiting.dto;

import com.goorm.thelastsupper.account.entity.Account;
import com.goorm.thelastsupper.waiting.entity.WaitingHistory;
import com.goorm.thelastsupper.waiting.entity.WaitingStatus;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record WaitingHistoryResponse(
    String waitingQueueId,
    String accountId,
    WaitingStatus waitingStatus,
    int headCount,
    Long number,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    String createdBy,
    String updatedBy) {
    public static WaitingHistoryResponse toWaitingHistoryResponse (WaitingHistory waitingHistory) {
        return WaitingHistoryResponse.builder()
                .waitingQueueId(waitingHistory.getId())
                .accountId(waitingHistory.getAccount().getId())
                .waitingStatus(waitingHistory.getWaitingStatus())
                .headCount(waitingHistory.getHeadCount())
                .number(waitingHistory.getNumber())
                .createdAt(waitingHistory.getCreatedAt())
                .updatedAt(waitingHistory.getUpdatedAt())
                .createdBy(waitingHistory.getCreatedBy())
                .updatedBy(waitingHistory.getUpdatedBy())
                .build();
    }
}
