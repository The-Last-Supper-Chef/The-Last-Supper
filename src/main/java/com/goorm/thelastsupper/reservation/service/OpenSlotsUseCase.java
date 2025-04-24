package com.goorm.thelastsupper.reservation.service;

import com.goorm.thelastsupper.reservation.component.ReservationPlanManagerService;
import com.goorm.thelastsupper.reservation.component.SlotOpeningPolicyService;
import com.goorm.thelastsupper.reservation.component.SlotPersistenceManagerService;
import com.goorm.thelastsupper.reservation.dto.OpenSlotsCommand;
import com.goorm.thelastsupper.reservation.dto.OpenSlotsResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpenSlotsUseCase {
	private final SlotOpeningPolicyService policyService;
	private final ReservationPlanManagerService planManagerService;

	@Transactional
	public OpenSlotsResponse execute(final OpenSlotsCommand cmd) {
		// 1) 규칙 검증
		policyService.validateRules(cmd);

		// 2) 플랜 생성 및 저장 (하루 단위로 예약 계획이 저장됨)
		var plans = planManagerService.createAndSave(cmd);

		// 4) 결과 반환
		return new OpenSlotsResponse(plans.get(0).getId(), plans.size());
	}
}
