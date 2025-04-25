package com.goorm.thelastsupper.waiting.service;

import com.goorm.thelastsupper.waiting.dto.WaitingResponse;
import com.goorm.thelastsupper.waiting.entity.WaitingQueue;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerCallService {
    private final CustomerCallValidationService customerCallValidationService;

    public WaitingResponse callWaiting() {
        WaitingQueue waitingQueue = customerCallValidationService.waitingQueueCall();

        return WaitingResponse.toWaitingResponse(waitingQueue);
    }
}
