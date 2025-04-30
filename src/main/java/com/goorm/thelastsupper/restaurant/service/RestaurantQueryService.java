package com.goorm.thelastsupper.restaurant.service;

import org.springframework.stereotype.Component;

import com.goorm.thelastsupper.restaurant.entity.Restaurant;
import com.goorm.thelastsupper.restaurant.repository.RestaurantRepository;

import lombok.RequiredArgsConstructor;

/**
 * 레스토랑 소유자 정보를 로드하는 컴포넌트입니다.
 */
@Component
@RequiredArgsConstructor
public class RestaurantQueryService {
	private final RestaurantRepository repository;
	public Restaurant getReferenceById(String ownerId) {
		return repository.getReferenceById(ownerId);
	}
}
