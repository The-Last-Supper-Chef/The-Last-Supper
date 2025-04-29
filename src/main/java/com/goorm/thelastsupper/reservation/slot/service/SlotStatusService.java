package com.goorm.thelastsupper.reservation.slot.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.goorm.thelastsupper.reservation.common.error.ReservationErrorCode;
import com.goorm.thelastsupper.reservation.common.exception.ReservationException;
import com.goorm.thelastsupper.reservation.plan.entity.SlotStatus;
import com.goorm.thelastsupper.reservation.slot.component.SlotReadService;
import com.goorm.thelastsupper.reservation.slot.component.SlotWriteService;
import com.goorm.thelastsupper.reservation.slot.entity.ReservationSlot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SlotStatusService {
	private final SlotReadService slotReadService;
	private final SlotWriteService slotWriteService;

	/**
	 * 특정 슬롯을 닫는 메서드
	 * @param slotId 닫을 슬롯의 ID
	 */
	public void changeSlotStatus(String slotId, SlotStatus status) {
		// 슬롯이 존재하는지 확인
		ReservationSlot slot = slotReadService.checkSlotExistence(slotId);

		// 슬롯 상태 변경: 닫힘 상태로 설정
		updateSlotStatus(slot, status);

		// 슬롯 저장
		try {
			slotWriteService.save(slot);
		} catch (Exception e) {
			throw new ReservationException(ReservationErrorCode.SLOT_SAVE_FAILURE, "슬롯 상태가 변경 시, 오류가 발생했습니다.");
		}
	}

	/**
	 * 슬롯 상태를 업데이트하는 메서드
	 * @param slot   슬롯 객체
	 * @param status 변경할 슬롯 상태
	 */
	private void updateSlotStatus(ReservationSlot slot, SlotStatus status) {
		// 상태에 맞게 변경
		switch (status) {
			case OPEN:
				slot.setOpen();
				break;
			case HOLD:
				slot.setHold();
				break;
			case BLOCK:
				slot.setBlock();
				break;
			default:
				log.error("Invalid slot status: {}", status);
				throw new ReservationException(ReservationErrorCode.SLOT_STATUS_NOT_FOUND,
					"해당 슬롯 상태는 "+status+"로서, 존재하지 않는 슬롯입니다.않습니다. OPEN, HOLD, BLOCK 중 하나여야 합니다.");

		}
	}
}
