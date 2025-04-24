package com.goorm.thelastsupper.reservation.component;

import org.springframework.stereotype.Component;

import com.goorm.thelastsupper.restaurant.entity.Restaurant;
import com.goorm.thelastsupper.restaurant.repository.RestaurantRepository;

import lombok.RequiredArgsConstructor;

/**
 * 레스토랑 소유자 정보를 로드하는 컴포넌트입니다.
 */
@Component
@RequiredArgsConstructor
public class RestaurantProviderServce {
	private final RestaurantRepository repo;
	public Restaurant loadOwner(String ownerId) {
		return repo.getReferenceById(ownerId);
	}
}
