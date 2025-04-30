package com.goorm.thelastsupper.reservation.plan.component;

import org.springframework.stereotype.Service;

import com.goorm.thelastsupper.reservation.plan.entity.ReservationPlan;
import com.goorm.thelastsupper.reservation.plan.repository.JpaPlanWriteRepository;
import com.goorm.thelastsupper.reservation.plan.repository.PlanWriteRepository;

import lombok.AllArgsConstructor;

/**
 * 예약 계획을 저장하는 서비스입니다.
 * - 결합: PlanJpaRepository
 * - 응집: 예약 계획 저장
 */
@Service
@AllArgsConstructor
public class PlanWriteService {
	private final JpaPlanWriteRepository planRepository;

	public ReservationPlan save(ReservationPlan plan) {
		return planRepository.save(plan);
	}
}
