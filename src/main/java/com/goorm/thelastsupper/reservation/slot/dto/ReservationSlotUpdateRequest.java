package com.goorm.thelastsupper.reservation.slot.dto;

import com.goorm.thelastsupper.reservation.plan.entity.SlotStatus;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ReservationSlotUpdateRequest(
	@NotBlank
	String slotId,

	@Min(value = 1, message = "총 수용 인원(capacityTotal)은 1 이상이어야 합니다.")
	int capacityTotal,

	@Min(value = 0, message = "remaining은 0 이상이어야 합니다.")
	int remaining,

	@NotNull
	SlotStatus status
) {}
