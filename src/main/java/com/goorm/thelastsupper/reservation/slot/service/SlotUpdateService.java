package com.goorm.thelastsupper.reservation.slot.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.goorm.thelastsupper.reservation.common.error.ReservationErrorCode;
import com.goorm.thelastsupper.reservation.common.exception.ReservationException;
import com.goorm.thelastsupper.reservation.slot.component.SlotReadService;
import com.goorm.thelastsupper.reservation.slot.component.SlotWriteService;
import com.goorm.thelastsupper.reservation.slot.dto.ReservationSlotResponse;
import com.goorm.thelastsupper.reservation.slot.dto.ReservationSlotUpdateRequest;
import com.goorm.thelastsupper.reservation.slot.entity.ReservationSlot;
import com.goorm.thelastsupper.reservation.slot.repository.ReservationTimeSlotRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SlotUpdateService {
	private final SlotReadService slotReadService;
	private final SlotWriteService slotWriteService;

	/**
	 * 전달받은 요청대로 슬롯 필드를 업데이트하고 저장한 뒤 DTO로 변환하여 반환합니다.
	 */
	@Transactional
	public List<ReservationSlotResponse> updateSlots(List<ReservationSlotUpdateRequest> requests) {
		return requests.stream()
			.map(this::processSingleUpdate)
			.map(ReservationSlot::toResponse)
			.toList();
	}

	/** 한 건의 업데이트 요청을 처리해서 저장된 엔티티를 반환 */
	private ReservationSlot processSingleUpdate(ReservationSlotUpdateRequest req) {
		// 1) Repository 접근 대신 읽기 컴포넌트를 통해 슬롯 조회
		ReservationSlot slot = slotReadService.checkSlotExistence(req.slotId());

		// 2) 엔티티에 필드 반영
		slot.updateCapacityAndStatus(
			req.capacityTotal(),
			req.remaining(),
			req.status()
		);

		// 3) 저장
		slotWriteService.save(slot);
		return slot;
	}
}
