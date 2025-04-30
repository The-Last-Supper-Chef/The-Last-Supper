package com.goorm.thelastsupper.reservation.plan.spec;

import org.springframework.stereotype.Component;

import com.goorm.thelastsupper.reservation.plan.dto.BreakInterval;
import com.goorm.thelastsupper.reservation.plan.dto.OpenSlotsCommandRequest;
import com.goorm.thelastsupper.reservation.common.error.ReservationErrorCode;
import com.goorm.thelastsupper.reservation.common.exception.ReservationException;

/**
 * 운영시간·휴식시간 규칙을 검증하는 Specification 구현체.
 */
@Component
public class TimeRuleSpecOpenSlotImpl implements OpenSlotValidationRule {

	@Override
	public void validate(OpenSlotsCommandRequest cmd) {

		/* 1) 운영 시작·종료 시간 유효성 검사 */
		validateOpenAndCloseTimes(cmd);

		/* 2) 예외 날짜 기능이 켜져 있으면 휴식 시간 검증을 모두 건너뜀 */
		if (cmd.isExceptionDateEnabled()) return;

		/* 3) 휴식 시간 존재 여부 확인 */
		validateBreakTimePresence(cmd);

		/* 4) 휴식 구간 간의 중복(겹침) 여부 확인 */
		validateBreakIntervalsNoOverlap(cmd);

		/* 5) 휴식 구간이 운영시간 범위 내에 포함되는지 확인 */
		validateBreakIntervalsWithinRange(cmd);
	}

	/* ------------------------------ private ------------------------------ */
	/** open, close 가 null 이 아니고 open < close 인지 확인 */
	private void validateOpenAndCloseTimes(OpenSlotsCommandRequest cmd) {
		if (cmd.openTime() == null || cmd.closeTime() == null || !cmd.openTime().isBefore(cmd.closeTime())) {
			String detail = String.format("open=%s, close=%s", cmd.openTime(), cmd.closeTime());
			throw new ReservationException(ReservationErrorCode.INVALID_OPERATION_TIME, detail);
		}
	}

	/** 휴식 구간이 비어 있으면 예외 */
	private void validateBreakTimePresence(OpenSlotsCommandRequest cmd) {
		if (cmd.breakTimes() == null || cmd.breakTimes().isEmpty()) {
			throw new ReservationException(
				ReservationErrorCode.INVALID_BREAK_TIME,
				"휴식 구간이 설정되지 않았습니다."
			);
		}
	}

	/** 휴식 구간들끼리 겹치는지(Overlap) 확인 */
	private void validateBreakIntervalsNoOverlap(OpenSlotsCommandRequest cmd) {
		for (int i = 0; i < cmd.breakTimes().size(); i++) {
			BreakInterval bi = cmd.breakTimes().get(i);
			for (int j = i + 1; j < cmd.breakTimes().size(); j++) {
				BreakInterval bj = cmd.breakTimes().get(j);
				if (bi.overlaps(bj)) {
					throw new ReservationException(ReservationErrorCode.BREAK_OVERLAP);
				}
			}
		}
	}

	/** 모든 휴식 구간이 운영시간 범위 안에 포함되는지 확인 */
	private void validateBreakIntervalsWithinRange(OpenSlotsCommandRequest cmd) {
		for (BreakInterval b : cmd.breakTimes()) {
			if (b.start().isBefore(cmd.openTime()) || b.end().isAfter(cmd.closeTime())) {
				String detail = String.format("break interval [%s~%s] out of range [%s~%s]",
					b.start(), b.end(), cmd.openTime(), cmd.closeTime());
				throw new ReservationException(ReservationErrorCode.BREAK_OUT_OF_RANGE, detail);
			}
		}
	}
}
