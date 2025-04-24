package com.goorm.thelastsupper.waiting.service;

import com.goorm.thelastsupper.waiting.dto.WaitingResponse;
import com.goorm.thelastsupper.waiting.entity.WaitingQueue;
import com.goorm.thelastsupper.account.entity.Account;
import com.goorm.thelastsupper.waiting.entity.WaitingStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerWaitingService {
    private final CustomerWaitingValidationService customerWaitingValidationService;

    public WaitingResponse createWaiting(String accountId, int headCount) {
        // accountId 기반으로 Account 엔티티 조회, 없으면 AccountNotFoundException throw
        Account account = customerWaitingValidationService.validateAccount(accountId);

        // WaitingSetCategory가 OPEN인지 조회, OPEN이 아니라면 WaitingNotOpenException throw
        customerWaitingValidationService.validateWaitingSetCategory();

        // Account 기반으로 WaitingQueue 엔티티 조회, WAITING 상태의 고객이 이미 있으면 AlreadyWaitingException throw
        customerWaitingValidationService.validateAlreadyWaiting(account);

        // 현재 최대 순서번호 + 1
        Long nextNumber = customerWaitingValidationService.findNextNumber();

        WaitingQueue waitingQueue = WaitingQueue.builder()
            .account(account)
            .headCount(headCount)
            .waitingStatus(WaitingStatus.WAITING)
            .number(nextNumber)
            .build();

        // WaitingQueue 저장
        waitingQueue = customerWaitingValidationService.waitingQueueSave(waitingQueue);

        return WaitingResponse.toWaitingResponse(waitingQueue);
    }

    public WaitingResponse cancelWaiting(String accountId) {
        // accountId 기반으로 Account 엔티티 조회, 없면 AccountNotFoundException throw
        Account account = customerWaitingValidationService.validateAccount(accountId);

        // Account 기반으로 WaitingQueue 엔티티 조회
        // WAITING 상태의 고객이 없으면 WaitingNotFoundException throw
        // 고객이 WAITING 중이면 WaitingStatus 를 CANCEL 로 변경
        WaitingQueue waitingQueue = customerWaitingValidationService.waitingQueueCancel(account);

        return WaitingResponse.toWaitingResponse(waitingQueue);
    }
}
