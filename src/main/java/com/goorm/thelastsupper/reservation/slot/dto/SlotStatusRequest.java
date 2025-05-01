package com.goorm.thelastsupper.reservation.slot.dto;

import com.goorm.thelastsupper.reservation.slot.entity.SlotStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SlotStatusRequest {
	private SlotStatus slotStatus;
}
