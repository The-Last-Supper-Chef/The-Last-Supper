package com.goorm.thelastsupper.reservation.plan.usecase;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.goorm.thelastsupper.reservation.plan.dto.ReservationPlanResponse;
import com.goorm.thelastsupper.reservation.plan.service.PlanWithSlotsQueryService;

import lombok.RequiredArgsConstructor;

/**
 * 유스케이스: 특정 식당의 예약 계획(Plan)과 슬롯(Slot)을 함께 조회
 */
@Component
@RequiredArgsConstructor
public class GetPlansWithSlotsUseCase {
	private final PlanWithSlotsQueryService planWithSlotsQueryService;

	/**
	 * @param restaurantId 조회할 식당 ID
	 * @param start        조회 시작일자(포함)
	 * @param end          조회 종료일자(포함)
	 * @return 예약 계획 + 슬롯 DTO 리스트
	 */
	@Transactional(readOnly = true)
	public List<ReservationPlanResponse> execute(
		String restaurantId,
		LocalDate start,
		LocalDate end
	) {
		return planWithSlotsQueryService.getPlansWithSlots(restaurantId, start, end);
	}
}
