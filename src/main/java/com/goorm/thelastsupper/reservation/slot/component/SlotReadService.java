package com.goorm.thelastsupper.reservation.slot.component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.goorm.thelastsupper.reservation.common.error.ReservationErrorCode;
import com.goorm.thelastsupper.reservation.common.exception.ReservationException;
import com.goorm.thelastsupper.reservation.slot.dto.ReservationSlotDTO;
import com.goorm.thelastsupper.reservation.slot.entity.ReservationSlot;
import com.goorm.thelastsupper.reservation.slot.repository.JpaSlotReadRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SlotReadService {
	private final JpaSlotReadRepository slotRepository;

	/**
	 * 1) 주어진 Plan ID 에 해당하는 슬롯만 골라서 DTO 로 변환한 뒤
	 *    Plan ID 별로 그룹핑한 맵을 리턴
	 */
	public Map<String, List<ReservationSlotDTO>> findSlotsGroupedByPlanId(List<String> planIds) {
		return slotRepository.findSlotsByPlanIds(planIds)
			.stream()
			.map(ReservationSlot::toDTO)
			.collect(Collectors.groupingBy(ReservationSlotDTO::planId));
	}

	/**
	 * 2) 주어진 슬롯 ID에 해당하는 슬롯을 리턴
	 */
	public ReservationSlot checkSlotExistence(String slotId) {
		return slotRepository.findById(slotId)
			.orElseThrow(() -> new ReservationException(
				ReservationErrorCode.RESERVATION_SLOT_NOT_FOUND, "해당 슬롯을 찾을 수 없습니다."));
	}

}

