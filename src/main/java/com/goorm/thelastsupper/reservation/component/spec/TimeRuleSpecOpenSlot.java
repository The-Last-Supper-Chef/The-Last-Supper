package com.goorm.thelastsupper.reservation.component.spec;

import org.springframework.stereotype.Component;

import com.goorm.thelastsupper.reservation.dto.BreakInterval;
import com.goorm.thelastsupper.reservation.dto.OpenSlotsCommand;
import com.goorm.thelastsupper.reservation.exception.ReservationErrorCode;
import com.goorm.thelastsupper.reservation.exception.ReservationException;

@Component
public class TimeRuleSpecOpenSlot implements OpenSlotValidationRule {
	/**
	 * 1) open < close 검사
	 * 2) 운영시간과 휴식시간이 겹치거나 경계가 동일한지 검사
	 * 3) 모든 휴식 구간이 운영시간 범위에 완전히 포함되는지 검사
	 *
	 * @param cmd	예약 슬롯 생성 명령 객체
	 * @throws ReservationException
	 *   - INVALID_OPERATION_TIME: 시작/종료 시간 부적절
	 *   - OPERATING_BREAK_OVERLAP: 운영시간·휴식시간 직접 충돌
	 *   - BREAK_OUT_OF_RANGE: 휴식구간이 운영시간 범위 밖
	 */
	@Override
	public void validate(OpenSlotsCommand cmd) {
		// 1) 운영 시작/종료 시간 유효성
		if (cmd.openTime() == null || cmd.closeTime() == null || !cmd.openTime().isBefore(cmd.closeTime())) {
			String detail = String.format("open=%s, close=%s", cmd.openTime(), cmd.closeTime());
			throw new ReservationException(ReservationErrorCode.INVALID_OPERATION_TIME, detail);
		}

		// 2) 휴식 구간이 운영시간 범위 내에 있는지 검사
		for (BreakInterval b : cmd.breakTimes()) {
			if (b.start().isBefore(cmd.openTime()) || b.end().isAfter(cmd.closeTime())) {
				String detail = String.format("break interval [%s~%s] out of range [%s~%s]",
					b.start(), b.end(), cmd.openTime(), cmd.closeTime());
				throw new ReservationException(ReservationErrorCode.BREAK_OUT_OF_RANGE, detail);
			}
		}
	}

}
