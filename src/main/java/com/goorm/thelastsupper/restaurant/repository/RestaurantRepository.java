package com.goorm.thelastsupper.restaurant.repository;

import com.goorm.thelastsupper.restaurant.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RestaurantRepository extends JpaRepository<Restaurant, String> {
    Optional<Restaurant> findByAccountId(String restaurantId);

    Optional<Restaurant> findByIdAndAccount_Id(String restaurantId, String accountId);
}
