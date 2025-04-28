package com.goorm.thelastsupper.reservation.plan.component;

import org.springframework.stereotype.Service;

import com.goorm.thelastsupper.reservation.plan.entity.ReservationPlan;
import com.goorm.thelastsupper.reservation.plan.repository.PlanReadJpaRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PlanWriteService {
	private final PlanReadJpaRepository planRepository;

	public ReservationPlan save(ReservationPlan plan) {
		return planRepository.save(plan);
	}
}
