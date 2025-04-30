package com.goorm.thelastsupper.reservation.plan.repository;

import com.goorm.thelastsupper.reservation.plan.entity.ReservationPlan;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 실제 데이터베이스와 상호작용하는 JPA 리포지토리입니다.
 */
public interface JpaPlanWriteRepository extends JpaRepository<ReservationPlan, String>, PlanWriteRepository {

	@Override
	ReservationPlan save(ReservationPlan plan);
}
