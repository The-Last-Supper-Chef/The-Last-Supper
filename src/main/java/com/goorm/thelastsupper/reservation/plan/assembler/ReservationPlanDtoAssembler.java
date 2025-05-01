package com.goorm.thelastsupper.reservation.plan.assembler;


import java.util.List;
import java.util.Map;
import java.util.Collections;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

import com.goorm.thelastsupper.reservation.plan.entity.ReservationPlan;
import com.goorm.thelastsupper.reservation.plan.dto.ReservationPlanResponse;
import com.goorm.thelastsupper.reservation.slot.dto.ReservationSlotDTO;

/**
 * ReservationPlan 엔티티와 슬롯 DTO 맵을 결합하여
 * 최종 ReservationPlanResponse 리스트를 생성하는 어셈블러
 */
@Slf4j
@Component
public class ReservationPlanDtoAssembler {

	/**
	 * Entity + 슬롯 맵을 결합하여 최종 ReservationPlanResponse 리스트 생성
	 *
	 * @param plans           예약 계획 엔티티 리스트
	 * @param slotsByPlanId   Plan ID별 슬롯 DTO 맵
	 * @return                ReservationPlanResponse 리스트
	 */
	public List<ReservationPlanResponse> assemble(
		List<ReservationPlan> plans,
		Map<String, List<ReservationSlotDTO>> slotsByPlanId
	) {
		return plans.stream()
			.map(plan -> {
				List<ReservationSlotDTO> slotDtos =
					slotsByPlanId.getOrDefault(plan.getId(), Collections.emptyList());
				log.info("planId={} → 슬롯 {}개", plan.getId(), slotDtos.size());
				return plan.toDTO();
			})
			.collect(Collectors.toList());
	}
}
