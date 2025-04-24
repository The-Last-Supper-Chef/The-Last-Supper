package com.goorm.thelastsupper.reservation.component.spec;

import org.springframework.stereotype.Component;

import com.goorm.thelastsupper.reservation.dto.BreakInterval;
import com.goorm.thelastsupper.reservation.dto.OpenSlotsCommand;
import com.goorm.thelastsupper.reservation.exception.ReservationErrorCode;
import com.goorm.thelastsupper.reservation.exception.ReservationException;

/**
 * 휴식 구간 간 중첩 규칙 검사 스펙
 */
@Component
public class BreakRuleSpecOpenSlot implements OpenSlotValidationRule {

	/**
	 * 휴식 구간이 서로 겹치는지 검사합니다.
	 *
	 * @throws ReservationException 휴식 구간이 겹칠 경우
	 */
	@Override
	public void validate(OpenSlotsCommand cmd) {
		if(cmd.isExceptionDateEnabled()){
			return; // 예외 날짜가 활성화된 경우, 휴식 구간 검증을 건너뜁니다.
		}
		// 휴식 구간이 비어 있을 경우 예외 처리
		if (cmd.breakTimes() == null || cmd.breakTimes().isEmpty()) {
			throw new ReservationException(ReservationErrorCode.INVALID_BREAK_TIME, "휴식 구간이 설정되지 않았습니다.");
		}
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
}
