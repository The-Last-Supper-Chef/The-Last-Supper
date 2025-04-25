package com.goorm.thelastsupper.waiting.controller;

import com.goorm.thelastsupper.waiting.dto.WaitingSettingRequest;
import com.goorm.thelastsupper.waiting.dto.WaitingSettingResponse;
import com.goorm.thelastsupper.waiting.service.WaitingSettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/waitings")
@RequiredArgsConstructor
public class WaitingSettingController {

    private final WaitingSettingService waitingSettingService;

    @PostMapping("/open")
    public ResponseEntity<WaitingSettingResponse> open(@RequestBody WaitingSettingRequest request) {
        WaitingSettingResponse response = waitingSettingService.openWaiting(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/close")
    public ResponseEntity<WaitingSettingResponse> close(@RequestBody WaitingSettingRequest request) {
        WaitingSettingResponse response = waitingSettingService.closeWaiting(request);
        return ResponseEntity.ok(response);
    }
}
