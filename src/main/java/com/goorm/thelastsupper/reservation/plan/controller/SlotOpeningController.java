package com.goorm.thelastsupper.reservation.plan.controller;

import java.util.List;

import com.goorm.thelastsupper.reservation.plan.dto.ReservationPlanResponse;
import com.goorm.thelastsupper.restaurant.exception.ApiResponse;
import com.goorm.thelastsupper.reservation.plan.dto.OpenSlotsCommandRequest;
import com.goorm.thelastsupper.reservation.plan.usecase.OpenSlotsUseCase;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 해당 클래스는 응답코드·메시지 세팅을 위한 컨트롤러입니다.
 * 그 외의 로직은 서비스 레이어에서 처리합니다.
 * 결합도: openSlotsUseCase
 */
@Validated
@RestController
@RequestMapping("/api/v1/slots")
@RequiredArgsConstructor
public class SlotOpeningController {
	private final OpenSlotsUseCase openSlotsUseCase;

	//@PreAuthorize("hasRole('OWNER')")
	@PostMapping("/open")
	public ResponseEntity<ApiResponse<List<ReservationPlanResponse>>> openSlots(@Valid @RequestBody OpenSlotsCommandRequest commandPayload) {
		List<ReservationPlanResponse> response = openSlotsUseCase.execute(commandPayload);
		return ResponseEntity.status(HttpStatus.CREATED).body(
			ApiResponse.success(response, "(점주용)슬롯이 성공적으로 열렸습니다."));
	}
}
