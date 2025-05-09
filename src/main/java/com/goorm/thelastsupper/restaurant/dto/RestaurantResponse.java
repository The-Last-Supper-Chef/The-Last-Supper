package com.goorm.thelastsupper.restaurant.dto;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Set;

import com.goorm.thelastsupper.restaurant.entity.Industry;
import com.goorm.thelastsupper.restaurant.entity.Restaurant;

import lombok.Builder;

@Builder
public record RestaurantResponse(
	String restaurantName,
	String restaurantLocation,
	Industry industry,
	String restaurantNumber,
	String introduction,
	LocalTime openTime,
	LocalTime closeTime,
	Set<DayOfWeek> openDays
) {

	public static RestaurantResponse toRestaurantResponse(Restaurant restaurant) {
		return RestaurantResponse.builder()
			.restaurantName(restaurant.getRestaurantName())
			.restaurantNumber(restaurant.getRestaurantNumber())
			.restaurantLocation(restaurant.getRestaurantLocation())
			.industry(restaurant.getIndustry())
			.introduction(restaurant.getIntroduction())
			.openTime(restaurant.getOpenTime())
			.closeTime(restaurant.getCloseTime())
			.openDays(restaurant.getWeekdays())
			.build();
	}
}
