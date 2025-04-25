package com.goorm.thelastsupper.waiting.service;

import com.goorm.thelastsupper.waiting.dto.WaitingResponse;
import com.goorm.thelastsupper.waiting.entity.WaitingQueue;
import com.goorm.thelastsupper.waiting.entity.WaitingStatus;
import com.goorm.thelastsupper.waiting.exception.WaitingException;
import com.goorm.thelastsupper.waiting.repository.WaitingQueueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerCallValidationService {
    private final WaitingQueueRepository waitingQueueRepository;

    public WaitingQueue waitingQueueCall() {
        WaitingQueue waitingQueue = waitingQueueRepository.findFirstByWaitingStatusOrderByCreatedAtAsc(WaitingStatus.WAITING)
                .orElseThrow(WaitingException.WaitingNotFoundException::new);

        waitingQueue.setWaitingStatus(WaitingStatus.SUCCESS);
        waitingQueueRepository.save(waitingQueue);

        return waitingQueue;
    }
}
