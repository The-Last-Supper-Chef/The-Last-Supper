package com.goorm.thelastsupper.reservation.plan.spec;

import com.goorm.thelastsupper.reservation.plan.dto.OpenSlotsCommandRequest;

/**
 * 예약 슬롯 오픈 정책을 검증하는 전략 인터페이스입니다.
 */
public interface OpenSlotValidationRule {
	void validate(OpenSlotsCommandRequest cmd);
}
