package com.goorm.thelastsupper.reservation.slot.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import com.goorm.thelastsupper.reservation.plan.entity.SlotStatus;
import com.goorm.thelastsupper.reservation.slot.entity.ReservationSlot;

public record ReservationSlotDTO(
	String    slotId,
	String    planId,
	LocalDate date,
	LocalTime startTime,
	int       capacityTotal,
	int       remaining,
	SlotStatus status
) {
	public static ReservationSlotDTO from(ReservationSlot s) {
		return new ReservationSlotDTO(
			s.getId(),
			s.getPlan().getId(),
			s.getDate(),
			s.getStartTime(),
			s.getCapacityTotal(),
			s.getRemaining(),
			s.getStatus()
		);
	}
}
