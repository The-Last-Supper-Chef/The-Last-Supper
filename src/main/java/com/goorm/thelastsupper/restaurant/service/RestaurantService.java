package com.goorm.thelastsupper.restaurant.service;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.goorm.thelastsupper.account.repository.AccountRepository;
import com.goorm.thelastsupper.account.service.AccountValidationService;
import com.goorm.thelastsupper.restaurant.dto.RestaurantResponse;
import com.goorm.thelastsupper.restaurant.dto.RestaurantUpdateRequest;
import com.goorm.thelastsupper.restaurant.entity.Industry;
import com.goorm.thelastsupper.restaurant.entity.Restaurant;
import com.goorm.thelastsupper.restaurant.repository.RestaurantRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class RestaurantService {

	private final RestaurantValidService restaurantValidService;
	private final RestaurantRepository restaurantRepository;
	private final AccountValidationService accountValidationService;

	public RestaurantResponse getRestaurant(String restaurantId) {
		return RestaurantResponse.toRestaurantResponse(restaurantValidService.findRestaurant(restaurantId));
	}

	@Transactional
	public RestaurantResponse updateRestaurant(String restaurantId, RestaurantUpdateRequest request, String ownerId) {
		Restaurant restaurant = restaurantValidService.findRestaurantByOwner(restaurantId, ownerId);
		restaurant.update(request);
		return RestaurantResponse.toRestaurantResponse(restaurant);
	}

	public void temp(String id) {
		restaurantRepository.save(Restaurant.builder()
			.account(accountValidationService.findById(id))
			.openTime(LocalTime.now())
			.closeTime(LocalTime.NOON)
			.industry(Industry.FINE_DINING)
			.introduction("소개글")
			.restaurantLocation("서울")
			.restaurantName("구수")
			.restaurantNumber("4")
			.weekdays(Set.of(DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY))
			.build());
	}
}
