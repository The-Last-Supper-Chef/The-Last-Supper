package com.goorm.thelastsupper.waiting.controller;

import com.goorm.thelastsupper.waiting.dto.WaitingResponse;
import com.goorm.thelastsupper.waiting.entity.WaitingQueue;
import com.goorm.thelastsupper.waiting.service.WaitingQueueService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/waitings")
@RequiredArgsConstructor
@Slf4j
public class WaitingQueueController {

    private final WaitingQueueService waitingQueueService;

    //대기중인 팀 리스트 조회
    @GetMapping("/queue")
    public List<WaitingResponse> getWaitingList() {
        return waitingQueueService.getWaitingList();
    }
}
