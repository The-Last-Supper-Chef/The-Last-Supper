package com.goorm.thelastsupper.reservation.plan.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.goorm.thelastsupper.reservation.plan.dto.ReservationPlanResponse;
import com.goorm.thelastsupper.reservation.plan.usecase.GetPlansWithSlotsUseCase;
import com.goorm.thelastsupper.restaurant.exception.ApiResponse;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequestMapping("/api/v1/restaurants")
@RequiredArgsConstructor
public class PlanReadController {
	private final GetPlansWithSlotsUseCase getPlansWithSlotsUseCase;

	/**
	 * 기간 내 예약 Plan (+ Slot) 조회
	 */
	@GetMapping("/{restaurantId}/plans")
	public ResponseEntity<ApiResponse<List<ReservationPlanResponse>>> findPlans(
		@PathVariable("restaurantId") String restaurantId,
		@RequestParam(name = "startDate") @NotNull
		@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
		@RequestParam(name = "endDate")   @NotNull
		@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
	) {
		var data = getPlansWithSlotsUseCase.execute(
			restaurantId, startDate, endDate
		);
		return ResponseEntity.ok(ApiResponse.success(data,"(점주용) " + startDate + " ~ " + endDate + " 예약 일정 조회 완료"));
	}
}
