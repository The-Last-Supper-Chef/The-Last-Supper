package com.goorm.thelastsupper.waiting.service;

import com.goorm.thelastsupper.waiting.dto.WaitingHistoryResponse;
import com.goorm.thelastsupper.waiting.dto.WaitingQueueResponse;
import com.goorm.thelastsupper.waiting.entity.WaitingQueue;
import com.goorm.thelastsupper.waiting.repository.WaitingQueueRepository;
import com.goorm.thelastsupper.waiting.repository.WaitingHistoryRepository;
import com.goorm.thelastsupper.waiting.entity.WaitingHistory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class WaitingGetService {
    private final WaitingQueueRepository waitingQueueRepository;
    private final WaitingHistoryRepository waitingHistoryRepository;

    public List<WaitingQueueResponse> getWaitingQueues() {
        return waitingQueueRepository.findAll().stream()
            .map(WaitingQueueResponse::toWaitingQueueResponse)
            .collect(Collectors.toList());
    }

    public List<WaitingHistoryResponse> getWaitingHistories() {
        return waitingHistoryRepository.findAll().stream()
            .map(WaitingHistoryResponse::toWaitingHistoryResponse)
            .collect(Collectors.toList());
    }
}
