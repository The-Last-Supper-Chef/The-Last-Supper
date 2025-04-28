package com.goorm.thelastsupper.reservation.common.exception;

import com.goorm.thelastsupper.reservation.common.error.ReservationErrorCode;

import lombok.Getter;

/**
 * 동일 시간 예약 슬롯이 이미 존재하는 경우 발생하는 예외.
 */
@Getter
public class SlotAlreadyExistsException extends ReservationException {

	// 기본 생성자
	public SlotAlreadyExistsException(String message) {
		super(ReservationErrorCode.DUPLICATE_SLOT_EXISTS, message);
	}

	// 에러 코드와 메시지를 받아서 생성하는 생성자
	public SlotAlreadyExistsException(ReservationErrorCode errorCode, String message) {
		super(errorCode, message);
	}

}
