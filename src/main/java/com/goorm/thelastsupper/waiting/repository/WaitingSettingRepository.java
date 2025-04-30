package com.goorm.thelastsupper.waiting.repository;

import com.goorm.thelastsupper.waiting.entity.WaitingSetCategory;
import com.goorm.thelastsupper.waiting.entity.WaitingSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WaitingSettingRepository extends JpaRepository<WaitingSetting, String> {

    boolean existsByRestaurant_IdAndWaitingSetCategory(String restaurantId, WaitingSetCategory category);

    Optional<WaitingSetting> findFirstByRestaurant_IdOrderByIdDesc(String restaurantId);

    // 전체 중에서 ULID ID 내림차순으로 정렬해 첫 번째(=가장 최신) 건을 반환
    Optional<WaitingSetting> findFirstByOrderByIdDesc();
}
