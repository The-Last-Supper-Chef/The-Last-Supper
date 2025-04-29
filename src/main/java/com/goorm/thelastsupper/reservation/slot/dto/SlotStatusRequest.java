package com.goorm.thelastsupper.reservation.slot.dto;

import com.goorm.thelastsupper.reservation.plan.entity.SlotStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SlotStatusRequest {
	private SlotStatus slotStatus;
}
