package com.goorm.thelastsupper.waiting.controller;

import com.goorm.thelastsupper.waiting.dto.WaitingSettingRequest;
import com.goorm.thelastsupper.waiting.dto.WaitingSettingResponse;
import com.goorm.thelastsupper.waiting.service.WaitingSettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/waitings")
@RequiredArgsConstructor
public class WaitingSettingController {

    private final WaitingSettingService waitingSettingService;

    @PostMapping("/open")
    public ResponseEntity<WaitingSettingResponse> create(@RequestBody WaitingSettingRequest request) {
        WaitingSettingResponse response = waitingSettingService.openWaiting(request);
        return ResponseEntity.ok(response);
    }

}
