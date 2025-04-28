package com.goorm.thelastsupper.reservation.plan.usecase;

import java.util.List;

import com.goorm.thelastsupper.reservation.plan.dto.OpenSlotsCommandRequest;
import com.goorm.thelastsupper.reservation.plan.dto.ReservationPlanResponse;
import com.goorm.thelastsupper.reservation.plan.service.PlanCreatorService;
import com.goorm.thelastsupper.reservation.plan.service.OpeningPolicyService;
import com.goorm.thelastsupper.reservation.slot.service.SlotCreatorService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OpenSlotsUseCase {
	private final OpeningPolicyService openingPolicyService;
	private final PlanCreatorService planCreatorService;
	private final SlotCreatorService slotCreatorService;

	@Transactional
	public List<ReservationPlanResponse> execute(final OpenSlotsCommandRequest cmd) {
		openingPolicyService.validateRules(cmd);			 // 1단계: 예약 슬롯 오픈 정책 검증
		var plans = planCreatorService.openPlans(cmd);       // 2단계: Plan 생성·저장
		return slotCreatorService.openSlots(plans, cmd);     // 3단계: Slot 생성·저장·DTO 변환
	}
}
