package com.goorm.thelastsupper.waiting.controller;

import com.goorm.thelastsupper.waiting.dto.WaitingResponse;
import com.goorm.thelastsupper.waiting.service.CustomerCallService;
import com.goorm.thelastsupper.waiting.service.CustomerWaitingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/waiting")
public class CustomerCallController {
    private final CustomerCallService customerCallService;

    @PostMapping("/call")
    public ResponseEntity<WaitingResponse> callWaiting() {
        WaitingResponse waitingResponse = customerCallService.callWaiting();

        return ResponseEntity.ok(waitingResponse);
    }
}
