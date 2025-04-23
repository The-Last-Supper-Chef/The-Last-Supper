package com.goorm.thelastsupper.reservation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/** 예약 슬롯 오픈 성공 응답 JSON */
public record OpenSlotsResponse(
	@JsonProperty("planId") String planId,
	@JsonProperty("slotCount") int slotCount
) {}
