package com.goorm.thelastsupper.reservation.controller;

import com.goorm.thelastsupper.reservation.dto.OpenSlotsCommand;
import com.goorm.thelastsupper.reservation.dto.OpenSlotsResponse;
import com.goorm.thelastsupper.reservation.service.OpenSlotsUseCase;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/v1/slots")
@RequiredArgsConstructor
public class SlotOpeningController {

	private final OpenSlotsUseCase openSlotsUseCase;

	@PostMapping("/open")
	// TODO: 추후 JWT로 점주 권한 체크 추가하기
	//@PreAuthorize("hasRole('OWNER')")
	public ResponseEntity<OpenSlotsResponse> openSlots(
		@Valid @RequestBody OpenSlotsCommand commandPayload
	) {
		OpenSlotsResponse result = openSlotsUseCase.execute(commandPayload);
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(new OpenSlotsResponse(result.planId(), result.slotCount()));
	}
}
