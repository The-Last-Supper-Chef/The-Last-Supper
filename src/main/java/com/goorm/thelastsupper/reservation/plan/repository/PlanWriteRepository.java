package com.goorm.thelastsupper.reservation.plan.repository;

import com.goorm.thelastsupper.reservation.plan.entity.ReservationPlan;

/**
 * 예약 Plan 저장을 위한 쓰기 전용 인터페이스.
 * 실제 데이터 접근은 인프라 레이어에서 구현됩니다.
 */
public interface PlanWriteRepository {

	/**
	 * 예약 계획을 저장하는 메서드.
	 * @param plan 예약 계획 객체
	 * @return 저장된 예약 계획
	 */
	ReservationPlan save(ReservationPlan plan);
}
