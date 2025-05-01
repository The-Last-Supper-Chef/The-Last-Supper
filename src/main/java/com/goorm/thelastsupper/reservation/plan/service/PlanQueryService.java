package com.goorm.thelastsupper.reservation.plan.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import com.goorm.thelastsupper.reservation.plan.entity.ReservationPlan;
import com.goorm.thelastsupper.reservation.plan.component.PlanReadService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlanQueryService {
	private final PlanReadService planReadService;

	public List<String> findPlanIds(String restaurantId, LocalDate start, LocalDate end) {
		return planReadService.findPlanIdsByRestaurantAndDateRange(restaurantId, start, end);
	}

	public List<ReservationPlan> findPlansByIds(List<String> planIds) {
		return planReadService.findAllById(planIds);
	}
}
