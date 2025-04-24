package com.goorm.thelastsupper.reservation.component.spec;

import com.goorm.thelastsupper.reservation.dto.OpenSlotsCommand;

/**
 * 예약 슬롯 오픈 정책을 검증하는 전략 인터페이스입니다.
 */
public interface OpenSlotValidationRule {
	void validate(OpenSlotsCommand cmd);
}
