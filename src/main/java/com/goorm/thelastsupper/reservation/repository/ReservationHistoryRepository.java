package com.goorm.thelastsupper.reservation.repository;

import com.goorm.thelastsupper.reservation.entity.ReservationHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationHistoryRepository extends JpaRepository<ReservationHistory, String> {
}
