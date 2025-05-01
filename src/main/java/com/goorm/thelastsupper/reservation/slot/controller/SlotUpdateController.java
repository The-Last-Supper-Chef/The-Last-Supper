package com.goorm.thelastsupper.reservation.slot.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.goorm.thelastsupper.reservation.slot.dto.ReservationSlotResponse;
import com.goorm.thelastsupper.reservation.slot.dto.ReservationSlotUpdateRequest;
import com.goorm.thelastsupper.reservation.slot.usecase.UpdateSlotsUseCase;
import com.goorm.thelastsupper.restaurant.exception.ApiResponse;

import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequestMapping("/api/v1/restaurants/restaurants/slots")
@RequiredArgsConstructor
public class SlotUpdateController {

	private final UpdateSlotsUseCase updateSlotsUseCase;

	/**
	 * 여러 슬롯의 capacityTotal, remaining, status 를 일괄 업데이트
	 */
	@PatchMapping
	public ResponseEntity<ApiResponse<List<ReservationSlotResponse>>> updateSlots(
		@Valid @RequestBody List<ReservationSlotUpdateRequest> requests
	) {
		var updatedSlots = updateSlotsUseCase.execute(requests);
		return ResponseEntity.ok(ApiResponse.success(updatedSlots, "슬롯 정보가 성공적으로 업데이트되었습니다."));
	}
}
