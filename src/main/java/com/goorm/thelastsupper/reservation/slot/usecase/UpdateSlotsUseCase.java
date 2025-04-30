package com.goorm.thelastsupper.reservation.slot.usecase;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.goorm.thelastsupper.reservation.slot.dto.ReservationSlotResponse;
import com.goorm.thelastsupper.reservation.slot.dto.ReservationSlotUpdateRequest;
import com.goorm.thelastsupper.reservation.slot.service.SlotUpdateService;

import lombok.RequiredArgsConstructor;

/**
 * 유스케이스: 여러 예약 슬롯의 capacity와 상태를 일괄 업데이트
 */
@Component
@RequiredArgsConstructor
public class UpdateSlotsUseCase {
	private final SlotUpdateService slotUpdateService;

	/**
	 * @param requests 여러 슬롯 업데이트 요청 DTO 리스트
	 * @return 업데이트된 슬롯을 DTO 로 변환한 리스트
	 */
	@Transactional
	public List<ReservationSlotResponse> execute(List<ReservationSlotUpdateRequest> requests) {
		return slotUpdateService.updateSlots(requests);
	}
}
