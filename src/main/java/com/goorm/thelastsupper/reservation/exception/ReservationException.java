package com.goorm.thelastsupper.reservation.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ReservationException extends RuntimeException{

	private final ReservationErrorCode errorCode;
	public ReservationException(ReservationErrorCode code, Throwable cause) {
		super(code.getMessage(), cause);
		this.errorCode = code;
	}


	/**
	 * 에러코드 메시지 뒤에 추가 설명을 붙일 수 있는 생성자
	 */
	public ReservationException(ReservationErrorCode errorCode, String detail) {
		super(errorCode.getMessage() + " – " + detail);
		this.errorCode = errorCode;
	}

}
