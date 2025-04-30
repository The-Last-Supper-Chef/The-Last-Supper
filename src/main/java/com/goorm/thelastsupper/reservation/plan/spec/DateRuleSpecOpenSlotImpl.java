package com.goorm.thelastsupper.reservation.plan.spec;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import com.goorm.thelastsupper.reservation.plan.dto.OpenSlotsCommandRequest;
import com.goorm.thelastsupper.reservation.common.error.ReservationErrorCode;
import com.goorm.thelastsupper.reservation.common.exception.ReservationException;

@Component
public class DateRuleSpecOpenSlotImpl implements OpenSlotValidationRule {

	/**
	 * 시작일과 예외 날짜 리스트에 대한 검증을 수행합니다.
	 * @param cmd 예약 명령 객체 (OpenSlotsCommand)
	 * @throws ReservationException 시작일이 내일 이전일 경우 또는 예외 날짜 리스트가 비어 있을 경우
	 */
	@Override
	public void validate(OpenSlotsCommandRequest cmd) {
		// 1) 시작일 검증
		validateStartDate(cmd.startDate());

		// 2) 시작일 이후에 종료일이 존재하는지 검증
		validateEndDate(cmd.startDate(), cmd.endDate());

		// 3) 예외 날짜 검증
		validateExceptionDates(cmd);
	}

	// 예약 시작일과 종료일이 유효한지 검증하는 private 메서드
	public void validateEndDate(LocalDate startDate, LocalDate endDate) {
		if (endDate.isBefore(startDate)) {
			String detail = String.format("startDate=%s, endDate=%s", startDate, endDate);
			throw new ReservationException(ReservationErrorCode.INVALID_END_DATE, detail);
		}
	}

	// 예약 시작일이 최소 내일 이후인지 검증하는 private 메서드
	private void validateStartDate(LocalDate startDate) {
		if (startDate.isBefore(LocalDate.now().plusDays(1))) {
			throw new ReservationException(ReservationErrorCode.START_DATE_TOO_SOON);
		}
	}

	// 예외 날짜 리스트가 비어 있으면 예외를 발생시키는 private 메서드
	private void validateExceptionDates(OpenSlotsCommandRequest cmd) {
		if (cmd.isExceptionDateEnabled() && (cmd.exceptionDates() == null || cmd.exceptionDates().isEmpty())) {
			throw new ReservationException(ReservationErrorCode.INVALID_EXCEPTION_DATE, "예외 날짜가 선택되었으나, 예외 날짜 리스트가 비어 있습니다.");
		}
	}
}
