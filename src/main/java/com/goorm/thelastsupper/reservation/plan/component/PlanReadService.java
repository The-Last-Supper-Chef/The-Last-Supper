package com.goorm.thelastsupper.reservation.plan.component;

import com.goorm.thelastsupper.reservation.plan.entity.ReservationPlan;
import com.goorm.thelastsupper.reservation.plan.repository.PlanReadJpaRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlanReadService {
	private final PlanReadJpaRepository planRepository;

	public List<String> findPlanIdsByRestaurantAndDateRange(String restaurantId, LocalDate start, LocalDate end) {
		return planRepository.findPlanIdsByRestaurantAndDateRange(restaurantId, start, end);
	}

	public List<ReservationPlan> findAllById(List<String> planIds) {
		return planRepository.findAllById(planIds);
	}

	public boolean existsByRestaurantIdAndPlanDate(String ownerId, LocalDate date) {
		return planRepository.existsByRestaurantIdAndPlanDate(ownerId, date);
	}
}
