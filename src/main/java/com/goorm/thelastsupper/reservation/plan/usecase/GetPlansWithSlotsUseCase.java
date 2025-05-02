package com.goorm.thelastsupper.reservation.plan.usecase;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.goorm.thelastsupper.reservation.plan.assembler.ReservationPlanDtoAssembler;
import com.goorm.thelastsupper.reservation.plan.dto.ReservationPlanResponse;
import com.goorm.thelastsupper.reservation.plan.service.PlanQueryService;
import com.goorm.thelastsupper.reservation.slot.service.SlotQueryService;

import lombok.RequiredArgsConstructor;

/**
 * 유스케이스: 특정 식당의 예약 계획(Plan)과 슬롯(Slot)을 함께 조회
 */
@Component
@RequiredArgsConstructor
public class GetPlansWithSlotsUseCase {
	private final PlanQueryService planQueryService;
	private final SlotQueryService slotQueryService;
	private final ReservationPlanDtoAssembler planDtoAssembler;

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
		// 1) Plan ID 조회
		var planIds = planQueryService.findPlanIds(restaurantId, start, end);
		if (planIds.isEmpty()) {
			return List.of();
		}
		// 2) Slot 조회 (Plan ID 별 그룹)
		var slotsByPlanId = slotQueryService.findSlotsByPlanIds(planIds);

		// 3) Plan 엔티티 조회
		var plans = planQueryService.findPlansByIds(planIds);

		// 4) DTO 조립
		return planDtoAssembler.assemble(plans, slotsByPlanId);
	}
}
