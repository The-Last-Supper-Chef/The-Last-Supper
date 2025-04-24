package com.goorm.thelastsupper.reservation.controller;

import com.goorm.thelastsupper.restaurant.exception.ApiResponse;
import com.goorm.thelastsupper.reservation.dto.OpenSlotsCommand;
import com.goorm.thelastsupper.reservation.dto.OpenSlotsResponse;
import com.goorm.thelastsupper.reservation.service.OpenSlotsUseCase;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/v1/slots")
@RequiredArgsConstructor
public class SlotOpeningController {

	private final OpenSlotsUseCase openSlotsUseCase;

	// TODO: 추후 JWT로 점주 권한 체크 추가하기
	//@PreAuthorize("hasRole('OWNER')")
	@PostMapping("/open")
	public ResponseEntity<ApiResponse<OpenSlotsResponse>> openSlots(
		@Valid @RequestBody OpenSlotsCommand commandPayload
	) {
		return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(
			openSlotsUseCase.execute(commandPayload), "슬롯이 성공적으로 열렸습니다."
		));
	}
}
