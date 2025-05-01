package com.goorm.thelastsupper.reservation.slot.service;

import org.springframework.stereotype.Component;

import com.goorm.thelastsupper.reservation.plan.dto.OpenSlotsCommandRequest;
import com.goorm.thelastsupper.reservation.plan.spec.DateRuleSpecOpenSlotImpl;
import com.goorm.thelastsupper.reservation.plan.spec.DayOfWeekRuleSpecOpenSlotImpl;
import com.goorm.thelastsupper.reservation.plan.spec.TimeRuleSpecOpenSlotImpl;

import lombok.AllArgsConstructor;

/**
 * 예약 슬롯 오픈 정책을 검증하는 컴포넌트입니다.
 * - 결합: DateRuleSpecOpenSlotImpl, TimeRuleSpecOpenSlotImpl, DayOfWeekRuleSpecOpenSlotImpl
 * - 응집: 예약 슬롯 오픈 정책 검증
 */
@Component
@AllArgsConstructor
public class OpeningPolicyService {
	private final DateRuleSpecOpenSlotImpl dateRuleSpec;
	private final TimeRuleSpecOpenSlotImpl timeRuleSpec;
	private final DayOfWeekRuleSpecOpenSlotImpl dayOfWeekRuleSpec;

	public void validateRules(OpenSlotsCommandRequest cmd) {
		// 1) 날짜 규칙: 시작일 유효성 및 예외 날짜 규칙
		dateRuleSpec.validate(cmd);

		// 2) 시간 규칙: 운영 시작/종료 시간 유효성 및 휴식 시간 규칙
		timeRuleSpec.validate(cmd);

		// 3) 요일 규칙: 예약 요일 체크
		dayOfWeekRuleSpec.validate(cmd);
	}
}
