package com.goorm.thelastsupper.reservation.repository;

import com.goorm.thelastsupper.reservation.entity.ReservationPlan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationPlanRepository extends JpaRepository<ReservationPlan,String> {
}
