package com.goorm.thelastsupper.restaurant.repository;

import com.goorm.thelastsupper.restaurant.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant, String> {
}
