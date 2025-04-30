package com.goorm.thelastsupper.waiting.service;

import com.goorm.thelastsupper.waiting.dto.WaitingResponse;
import com.goorm.thelastsupper.waiting.entity.WaitingQueue;
import com.goorm.thelastsupper.waiting.entity.WaitingStatus;
import com.goorm.thelastsupper.waiting.repository.WaitingQueueRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class WaitingQueueService {

    private final WaitingQueueRepository waitingQueueRepository;

    @Transactional
    public List<WaitingResponse> getWaitingList() {
        // 대기중인 웨이팅 리스트 조회
        List<WaitingQueue> waitingQueues = waitingQueueRepository.findAllByWaitingStatus(WaitingStatus.WAITING);

        // WaitingQueue를 WaitingResponse로 변환한 리스트를 저장할 리스트 선언
        List<WaitingResponse> waitingResponses = new ArrayList<>();

        // 각 WaitingQueue를 WaitingResponse로 변환하여 리스트에 추가
        waitingResponses = waitingQueues.stream()
                .map(WaitingResponse::toWaitingResponse)
                .collect(Collectors.toList());

        //변환된 리스트 반환
        return waitingResponses;
    }
}
