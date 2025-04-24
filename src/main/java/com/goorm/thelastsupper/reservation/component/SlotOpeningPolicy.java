package com.goorm.thelastsupper.reservation.component;

import org.springframework.stereotype.Component;

import com.goorm.thelastsupper.reservation.component.spec.BreakRuleSpecOpenSlot;
import com.goorm.thelastsupper.reservation.component.spec.DateRuleSpecOpenSlot;
import com.goorm.thelastsupper.reservation.component.spec.DayOfWeekRuleSpecOpenSlot;
import com.goorm.thelastsupper.reservation.component.spec.TimeRuleSpecOpenSlot;
import com.goorm.thelastsupper.reservation.dto.OpenSlotsCommand;

import lombok.AllArgsConstructor;

/**
 * 예약 슬롯 오픈 정책을 검증하는 컴포넌트입니다.
 */
@Component
@AllArgsConstructor
public class SlotOpeningPolicy {
	private final DateRuleSpecOpenSlot dateRuleSpec;
	private final TimeRuleSpecOpenSlot timeRuleSpec;
	private final DayOfWeekRuleSpecOpenSlot dayOfWeekRuleSpec;
	private final BreakRuleSpecOpenSlot breakRuleSpec;

	public void validateRules(OpenSlotsCommand cmd) {
		// 1) 날짜 규칙: 시작일 유효성 및 예외 날짜 규칙
		dateRuleSpec.validate(cmd);

		// 2) 시간 규칙: 운영 시작/종료 시간 유효성 및 휴식 시간 규칙
		timeRuleSpec.validate(cmd);

		// 3) 요일 규칙: 예약 요일 체크
		dayOfWeekRuleSpec.validate(cmd);

		// 4) 휴식 중첩 규칙: 휴식 구간 간 중첩 검사
		breakRuleSpec.validate(cmd);
	}
}
