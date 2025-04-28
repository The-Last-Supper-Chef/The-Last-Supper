package com.goorm.thelastsupper.reservation.plan.spec;

import com.goorm.thelastsupper.reservation.plan.dto.OpenSlotsCommandRequest;
import com.goorm.thelastsupper.reservation.common.error.ReservationErrorCode;
import com.goorm.thelastsupper.reservation.common.exception.ReservationException;
import org.springframework.stereotype.Component;

/**
 * 요일 규칙 검사 스펙
 * - 반복 요일이 비어있지 않도록 검사합니다.
 */
@Component
public class DayOfWeekRuleSpecOpenSlotImpl implements OpenSlotValidationRule {

	/**
	 * 예약 요일이 비어있는지 확인하고 예외를 발생시킵니다.
	 * @param cmd 	예약 명령 객체 (OpenSlotsCommand)
	 * @throws ReservationException 반복 요일이 비어있는 경우
	 */
	@Override
	public void validate(OpenSlotsCommandRequest cmd) {
		if (cmd.dayOfWeekBased()) {
			validateRepeatDays(cmd);
		}
	}

	private void validateRepeatDays(OpenSlotsCommandRequest cmd) {
		if (cmd.repeatDays() == null || cmd.repeatDays().isEmpty()) {
			throw new ReservationException(ReservationErrorCode.INVALID_DAY_OF_WEEK, "반복 요일이 비어 있습니다.");
		}
	}
}
