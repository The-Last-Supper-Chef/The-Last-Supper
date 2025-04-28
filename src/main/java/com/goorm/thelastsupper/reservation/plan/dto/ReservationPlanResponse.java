package com.goorm.thelastsupper.reservation.plan.dto;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.goorm.thelastsupper.reservation.slot.dto.ReservationSlotDTO;

public record ReservationPlanResponse(
	LocalDate             planDate,
	DayOfWeek             weekday,
	LocalTime             openTime,
	LocalTime             closeTime,
	LocalTime             breakOpenTime,
	LocalTime             breakCloseTime,
	long                  turnTimeMinutes,
	List<ReservationSlotDTO> slots
) {}
