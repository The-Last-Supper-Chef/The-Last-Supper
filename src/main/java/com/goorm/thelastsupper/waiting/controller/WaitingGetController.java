package com.goorm.thelastsupper.waiting.controller;

import com.goorm.thelastsupper.waiting.dto.WaitingHistoryResponse;
import com.goorm.thelastsupper.waiting.dto.WaitingQueueResponse;
import com.goorm.thelastsupper.waiting.service.WaitingGetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/waiting/get")
public class WaitingGetController {
    private final WaitingGetService waitingGetService;

    @GetMapping("/waiting-queues")
    public List<WaitingQueueResponse> getWaitingQueues() {
        return waitingGetService.getWaitingQueues();
    }

    @GetMapping("/waiting-histories")
    public List<WaitingHistoryResponse> getWaitingHistories() {
        return waitingGetService.getWaitingHistories();
    }
}
