package com.goorm.thelastsupper.reservation.slot.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import com.goorm.thelastsupper.reservation.slot.dto.ReservationSlotDTO;
import com.goorm.thelastsupper.reservation.slot.repository.ReservationSlotJpaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SlotReadService {
	private final ReservationSlotJpaRepository slotRepository;

	/**
	 * 1) 주어진 Plan ID 에 해당하는 슬롯만 골라서 DTO 로 변환한 뒤
	 *    Plan ID 별로 그룹핑한 맵을 리턴
	 */
	public Map<String, List<ReservationSlotDTO>> findSlotsGroupedByPlanId(List<String> planIds) {
		return slotRepository.findSlotsByPlanIds(planIds)
			.stream()
			.map(ReservationSlotDTO::from)
			.collect(Collectors.groupingBy(ReservationSlotDTO::planId));
	}

}

