package com.goorm.thelastsupper.restaurant.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.goorm.thelastsupper.common.security.CustomPrincipal;
import com.goorm.thelastsupper.restaurant.dto.RestaurantResponse;
import com.goorm.thelastsupper.restaurant.dto.RestaurantUpdateRequest;
import com.goorm.thelastsupper.restaurant.service.RestaurantService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/api/v1/restaurants")
@RequiredArgsConstructor
@Validated
public class RestaurantController {

	private final RestaurantService restaurantService;

	@GetMapping("/{restaurantId}")
	public ResponseEntity<RestaurantResponse> getAccount(@PathVariable("restaurantId") String restaurantId) {
		RestaurantResponse response = restaurantService.getRestaurant(restaurantId);
		return ResponseEntity.ok(response);
	}

	@PreAuthorize("hasAuthority('OWNER')")
	@PutMapping("/{restaurantId}")
	public ResponseEntity<RestaurantResponse> updateRestaurant(@PathVariable("restaurantId") String restaurantId,
		@RequestBody RestaurantUpdateRequest request, @AuthenticationPrincipal CustomPrincipal customPrincipal) {
		RestaurantResponse response = restaurantService.updateRestaurant(restaurantId,request,customPrincipal.getId());
		return ResponseEntity.ok(response);
	}

	@PostMapping
	public ResponseEntity<Void> createTemp(@AuthenticationPrincipal CustomPrincipal customPrincipal) {
		restaurantService.temp(customPrincipal.getId());
		return ResponseEntity.ok().build();
	}
}
