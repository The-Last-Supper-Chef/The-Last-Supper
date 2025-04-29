package com.goorm.thelastsupper.reservation.plan.repository;

import java.time.LocalDate;
import java.util.List;

/**
 * 예약 Plan 조회를 위한 도메인 레이어 인터페이스.
 * 실제 데이터 접근은 인프라 레이어에서 구현됩니다.
 */
public interface PlanReadRepository {

	/**
	 * 특정 레스토랑과 날짜 범위에 맞는 예약 계획 ID 목록을 조회하는 메서드
	 * @param restaurantId 레스토랑 ID
	 * @param start 시작 날짜
	 * @param end 종료 날짜
	 * @return 예약 계획 ID 목록
	 */
	List<String> findPlanIdsByRestaurantAndDateRange(String restaurantId, LocalDate start, LocalDate end);

	/**
	 * 특정 레스토랑과 날짜에 해당하는 예약 계획이 존재하는지 여부를 조회하는 메서드.
	 * @param restaurantId 레스토랑 ID
	 * @param planDate 예약 날짜
	 * @return 존재 여부
	 */
	boolean existsByRestaurantIdAndPlanDate(String restaurantId, LocalDate planDate);
}
