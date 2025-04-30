package com.goorm.thelastsupper.reservation.slot.usecase;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.goorm.thelastsupper.reservation.common.error.ReservationErrorCode;
import com.goorm.thelastsupper.reservation.common.exception.ReservationException;
import com.goorm.thelastsupper.reservation.plan.entity.SlotStatus;
import com.goorm.thelastsupper.reservation.slot.service.SlotStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SlotStatusUsecase {
	private final SlotStatusService slotStatusService;

	/**
	 * 특정 슬롯의 상태를 변경하는 메서드
	 */
	@Transactional()
	public void execute(String slotId, SlotStatus slotStatus) {
		try {
			slotStatusService.changeSlotStatus(slotId, slotStatus);
		} catch (IllegalArgumentException e) {
			throw new ReservationException(ReservationErrorCode.SLOT_STATUS_NOT_FOUND,
				String.valueOf(org.springframework.http.HttpStatus.BAD_REQUEST));
		}
	}
}

