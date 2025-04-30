package com.goorm.thelastsupper.reservation.slot.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import com.goorm.thelastsupper.reservation.plan.entity.SlotStatus;
/**
 * 예약 슬롯 정보 응답 DTO
 */
public record ReservationSlotResponse(
	String slotId,
	String planId,
	LocalDate date,
	LocalTime startTime,
	int capacityTotal,
	int remaining,
	SlotStatus status
) {
}
