package com.goorm.thelastsupper.reservation.slot.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import com.goorm.thelastsupper.reservation.slot.entity.SlotStatus;

public record ReservationSlotDTO(
	String    slotId,
	String    planId,
	LocalDate date,
	LocalTime startTime,
	int       capacityTotal,
	int       remaining,
	SlotStatus status
) {
}
