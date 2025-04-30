package com.goorm.thelastsupper.reservation.slot.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.goorm.thelastsupper.reservation.plan.dto.OpenSlotsCommandRequest;
import com.goorm.thelastsupper.reservation.plan.dto.ReservationPlanResponse;
import com.goorm.thelastsupper.reservation.plan.entity.ReservationPlan;
import com.goorm.thelastsupper.reservation.slot.component.SlotWriteService;

import lombok.RequiredArgsConstructor;

/**
 * 해당 클래스는 예약 슬롯을 생성하는 서비스입니다.
 * - 결합: SlotWriteService
 * - 응집: 예약 슬롯 생성 및 dto 변환
 */
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
