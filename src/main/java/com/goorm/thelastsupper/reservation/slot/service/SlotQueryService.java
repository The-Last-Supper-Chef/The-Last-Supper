package com.goorm.thelastsupper.reservation.slot.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.goorm.thelastsupper.reservation.slot.component.SlotReadService;
import com.goorm.thelastsupper.reservation.slot.dto.ReservationSlotDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SlotQueryService {
	private final SlotReadService slotReadService;

	public Map<String, List<ReservationSlotDTO>> findSlotsByPlanIds(List<String> planIds) {
		return slotReadService.findSlotsGroupedByPlanId(planIds);
	}
}
