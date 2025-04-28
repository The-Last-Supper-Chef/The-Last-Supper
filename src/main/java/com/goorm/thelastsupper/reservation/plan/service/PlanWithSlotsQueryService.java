package com.goorm.thelastsupper.reservation.plan.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.goorm.thelastsupper.reservation.plan.dto.ReservationPlanResponse;
import com.goorm.thelastsupper.reservation.plan.entity.ReservationPlan;
import com.goorm.thelastsupper.reservation.plan.component.PlanReadService;
import com.goorm.thelastsupper.reservation.slot.dto.ReservationSlotDTO;
import com.goorm.thelastsupper.reservation.slot.service.SlotReadService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlanWithSlotsQueryService {
	private final PlanReadService planReadService;
	private final SlotReadService slotReadService;

	@Transactional(readOnly = true)
	public List<ReservationPlanResponse> getPlansWithSlots(
		String restaurantId,
		LocalDate start,
		LocalDate end
	) {
		// 1) Plan ID 조회
		List<String> planIds = planReadService.findPlanIdsByRestaurantAndDateRange(restaurantId, start, end);
		if (planIds.isEmpty()) {
			return List.of();
		}

		// 2) 슬롯 맵 조회
		Map<String, List<ReservationSlotDTO>> slotsByPlanId = slotReadService.findSlotsGroupedByPlanId(planIds);

		// 3) Plan 엔티티 조회
		List<ReservationPlan> plans =  planReadService.findAllById(planIds);

		// 4) DTO 조립
		return assembleDtos(plans, slotsByPlanId);
	}

	// ——————————————————————————————————————————————
	// private helper methods
	// ——————————————————————————————————————————————


	/** Entity + 슬롯 맵을 결합하여 최종 ReservationPlanDTO 리스트 생성 */
	private List<ReservationPlanResponse> assembleDtos(
		List<ReservationPlan> plans,
		Map<String, List<ReservationSlotDTO>> slotsByPlanId
	) {
		return plans.stream()
			.map(plan -> {
				List<ReservationSlotDTO> slotDtos =
					slotsByPlanId.getOrDefault(plan.getId(), List.of());
				log.info("planId={} → 슬롯 {}개", plan.getId(), slotDtos.size());
				return new ReservationPlanResponse(
					plan.getPlanDate(),
					plan.getWeekday(),
					plan.getOpenTime(),
					plan.getCloseTime(),
					plan.getBreakOpenTime(),
					plan.getBreakCloseTime(),
					plan.getTurnTimeMinutes(),
					slotDtos
				);
			})
			.collect(Collectors.toList());
	}
}
