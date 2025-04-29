package com.goorm.thelastsupper.reservation.common.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ReservationErrorCode {
    RESERVATION_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 예약을 찾을 수 없습니다."),

	// 슬롯 오픈 관련 에러
	START_DATE_TOO_SOON(HttpStatus.UNPROCESSABLE_ENTITY, "시작일은 최소 내일 이후여야 합니다."),
	INVALID_EXCEPTION_DATE(HttpStatus.UNPROCESSABLE_ENTITY, "예외 날짜가 선택되었으나, 예외 날짜 리스트가 비어 있습니다."),
	INVALID_OPERATION_TIME(HttpStatus.UNPROCESSABLE_ENTITY, "영업 시작 시간은 종료 시간 이전이어야 합니다."),
	OPERATING_BREAK_OVERLAP(HttpStatus.UNPROCESSABLE_ENTITY, "운영 시간과 휴식 시간이 겹칩니다."),
	INVALID_BREAK_TIME(HttpStatus.UNPROCESSABLE_ENTITY, "휴식 구간이 설정되지 않았습니다."),
	BREAK_OUT_OF_RANGE(HttpStatus.UNPROCESSABLE_ENTITY, "휴식 구간이 운영 시간 범위를 벗어납니다."),
	BREAK_OVERLAP(HttpStatus.UNPROCESSABLE_ENTITY, "휴식 구간이 서로 겹칩니다."),
	BREAK_TIME_INVALID(HttpStatus.UNPROCESSABLE_ENTITY, "휴식 시작 시간은 종료 시간보다 빨라야 합니다."),
	INVALID_DAY_OF_WEEK(HttpStatus.UNPROCESSABLE_ENTITY, "예약 요일이 비어 있습니다."),
	DUPLICATE_SLOT_EXISTS     (HttpStatus.CONFLICT,               "해당 시간 슬롯은 이미 존재합니다."),
	SLOT_SAVE_FAILURE         (HttpStatus.INTERNAL_SERVER_ERROR,  "슬롯 저장 중 오류가 발생했습니다."),
	INVALID_END_DATE(HttpStatus.UNPROCESSABLE_ENTITY, "예약 종료일이 시작일보다 이전입니다."),
	INVALID_PERIOD(HttpStatus.UNPROCESSABLE_ENTITY, "예약 기간이 유효하지 않습니다."),
	RESERVATION_PLAN_ALREADY_EXISTS(HttpStatus.UNPROCESSABLE_ENTITY, "예약 일정이 이미 존재합니다."),
	RESERVATION_PLAN_NOT_FOUND(HttpStatus.NOT_FOUND, "예약 일정이 존재하지 않습니다."),
	RESERVATION_ACCOUNT_NOT_FOUND(HttpStatus.NOT_FOUND, "예약에 연결된 계정이 존재하지 않습니다."),
	RESERVATION_ACCOUNT_MISMATCH(HttpStatus.FORBIDDEN, "본인 예약만 취소할 수 있습니다."),
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
