package com.goorm.thelastsupper.reservation.plan.repository;

import com.goorm.thelastsupper.reservation.plan.entity.ReservationPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * 실제 데이터베이스와 상호작용하는 JPA 리포지토리입니다.
 */
public interface JpaPlanReadRepository
	extends JpaRepository<ReservationPlan, String>, PlanReadRepository {

	/**
	 * reservation_plan 테이블에서 ID 목록만 꺼냅니다.
	 */
	@Query(value = """
      SELECT p.id
        FROM reservation_plan p
       WHERE p.restaurant_id = :restaurantId
         AND p.plan_date BETWEEN :start AND :end
      """, nativeQuery = true)
	@Override
	List<String> findPlanIdsByRestaurantAndDateRange(@Param("restaurantId") String restaurantId,
		@Param("start") LocalDate start,
		@Param("end") LocalDate end);

	@Override
	boolean existsByRestaurantIdAndPlanDate(String restaurantId, LocalDate planDate);
}
