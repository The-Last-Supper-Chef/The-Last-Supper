package com.goorm.thelastsupper.reservation.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ReservationErrorCode {

    RESERVATION_NOT_FOUND(HttpStatus.NOT_FOUND, "예약 정보를 찾을 수 없습니다."),
    RESERVATION_SLOT_NOT_FOUND(HttpStatus.NOT_FOUND, "예약 슬롯이 존재하지 않습니다."),
    RESERVATION_SLOT_CLOSED(HttpStatus.FORBIDDEN, "현재 예약이 불가능한 슬롯입니다."),
    RESERVATION_DUPLICATE_RESERVATION(HttpStatus.UNPROCESSABLE_ENTITY, "이미 예약이 존재합니다."),
    RESERVATION_TIME_EXPIRED(HttpStatus.UNPROCESSABLE_ENTITY, "예약 가능 시간이 만료되었습니다."),
    RESERVATION_CAPACITY_OVER(HttpStatus.UNPROCESSABLE_ENTITY, "예약 가능 인원을 초과했습니다."),
    RESERVATION_INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Repository save 실패" ),
    RESERVATION_ID_MISMATCH(HttpStatus.BAD_REQUEST, "요청한 예약 ID와 일치하지 않습니다.");
    private final HttpStatus httpStatus;
    private final String message;
}
