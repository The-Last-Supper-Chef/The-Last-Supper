package com.goorm.thelastsupper.reservation.plan.dto;
import java.time.LocalTime;

import com.goorm.thelastsupper.reservation.common.error.ReservationErrorCode;
import com.goorm.thelastsupper.reservation.common.exception.ReservationException;

/**
 * 휴식 구간 값 객체 (불변).
 *
 * <p>start &lt; end 불변식을 보장하며,
 * 구간 중첩 여부를 판단하는 {@code overlaps(...)} 유틸을 제공한다.</p>
 */
public record BreakInterval(LocalTime start, LocalTime end) {

	/* ── 불변식 검사 ───────────────────────────── */
	public BreakInterval {
		if (start == null || end == null || !start.isBefore(end)) {
			throw new ReservationException(ReservationErrorCode.BREAK_TIME_INVALID);
		}
	}

	/* ── 유틸 ─────────────────────────────────── */
	/** 다른 구간(start~end)과 겹치는지 검사 */
	public boolean overlaps(LocalTime otherStart, LocalTime otherEnd) {
		return start.isBefore(otherEnd) && end.isAfter(otherStart);
	}

	/** 다른 BreakInterval 과 겹치는지 검사 */
	public boolean overlaps(BreakInterval other) {
		return overlaps(other.start, other.end);
	}
}
