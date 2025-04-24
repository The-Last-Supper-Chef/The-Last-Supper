package com.goorm.thelastsupper.reservation.service;

import com.goorm.thelastsupper.reservation.component.ReservationPlanManager;
import com.goorm.thelastsupper.reservation.component.SlotOpeningPolicy;
import com.goorm.thelastsupper.reservation.component.SlotPersistenceManager;
import com.goorm.thelastsupper.reservation.dto.OpenSlotsCommand;
import com.goorm.thelastsupper.reservation.dto.OpenSlotsResponse;
import com.goorm.thelastsupper.reservation.entity.ReservationPlan;
import com.goorm.thelastsupper.reservation.entity.ReservationSlot;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpenSlotsService {
	private final SlotOpeningPolicy policy;
	private final ReservationPlanManager planManager;
	private final SlotPersistenceManager slotManager;

	@Transactional
	public OpenSlotsResponse execute(final OpenSlotsCommand cmd) {
		// 1) 도메인 규칙 검증
		policy.validateRules(cmd);

		// 2) 플랜 생성 및 저장 (하루 단위로 예약 계획이 저장됨)
		List<ReservationPlan> plans = planManager.createAndSave(cmd);

		// 3) 슬롯 생성 및 저장
		for (ReservationPlan plan : plans) {
			List<ReservationSlot> slots = plan.createSlots(cmd);
			slotManager.persist(slots);  // 슬롯 저장
		}

		// 4) 결과 반환
		return new OpenSlotsResponse(plans.get(0).getId(), plans.size());
	}
}
