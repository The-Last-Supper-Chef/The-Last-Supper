package com.goorm.thelastsupper.reservation.plan.component;

import com.goorm.thelastsupper.reservation.plan.entity.ReservationPlan;
import com.goorm.thelastsupper.reservation.plan.repository.JpaPlanReadRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * 예약 계획을 읽어오는 서비스입니다.
 * - 결합: PlanReadJpaRepository
 * - 응집: 예약 계획 조회
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PlanReadService {
	private final JpaPlanReadRepository planReadRepository;

	public List<String> findPlanIdsByRestaurantAndDateRange(String restaurantId, LocalDate start, LocalDate end) {
		return planReadRepository.findPlanIdsByRestaurantAndDateRange(restaurantId, start, end);
	}

	public List<ReservationPlan> findAllById(List<String> planIds) {
		return planReadRepository.findAllById(planIds);
	}

	public boolean existsByRestaurantIdAndPlanDate(String ownerId, LocalDate date) {
		return planReadRepository.existsByRestaurantIdAndPlanDate(ownerId, date);
	}
}
