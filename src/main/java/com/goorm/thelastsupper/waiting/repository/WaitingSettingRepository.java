package com.goorm.thelastsupper.waiting.repository;

import com.goorm.thelastsupper.waiting.entity.WaitingSetCategory;
import com.goorm.thelastsupper.waiting.entity.WaitingSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WaitingSettingRepository extends JpaRepository<WaitingSetting, String> {

    boolean existsByRestaurant_IdAndWaitingSetCategory(String restaurantId, WaitingSetCategory category);
}
