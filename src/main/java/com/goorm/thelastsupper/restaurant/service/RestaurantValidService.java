package com.goorm.thelastsupper.restaurant.service;

import org.springframework.stereotype.Service;

import com.goorm.thelastsupper.restaurant.entity.Restaurant;
import com.goorm.thelastsupper.restaurant.exception.RestaurantException;
import com.goorm.thelastsupper.restaurant.repository.RestaurantRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class RestaurantValidService {

	private final RestaurantRepository restaurantRepository;

	public Restaurant findRestaurant(String id) {
		return restaurantRepository.findById(id).orElseThrow(RestaurantException.RestaurantNotFoundException::new);
	}

	public Restaurant findRestaurantByOwner(String restaurantId, String ownerId) {
		return restaurantRepository.findByIdAndAccount_Id(restaurantId, ownerId)
			.orElseThrow(RestaurantException.RestaurantNotFoundException::new);
	}
}
