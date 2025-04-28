package com.goorm.thelastsupper.reservation.slot.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.goorm.thelastsupper.reservation.plan.dto.OpenSlotsCommandRequest;
import com.goorm.thelastsupper.reservation.plan.dto.ReservationPlanResponse;
import com.goorm.thelastsupper.reservation.plan.entity.ReservationPlan;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SlotCreatorService {
	private final SlotWriteService slotWriteService;

	public List<ReservationPlanResponse> openSlots(List<ReservationPlan> plans, OpenSlotsCommandRequest cmd) {
		return plans.stream()
			.peek(plan -> slotWriteService.persist(plan.createSlots(cmd)))
			.map(ReservationPlan::toDTO)
			.toList();
	}
}
