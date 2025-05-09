package com.goorm.thelastsupper.restaurant.dto;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Set;

import com.goorm.thelastsupper.restaurant.entity.Industry;

import jakarta.validation.constraints.NotBlank;

public record RestaurantUpdateRequest(
	String restaurantName,
	String restaurantLocation,
	Industry industry,
	String restaurantNumber,
	String introduction,
	LocalTime openTime,
	LocalTime closeTime,
	Set<DayOfWeek> openDays
) {
}
