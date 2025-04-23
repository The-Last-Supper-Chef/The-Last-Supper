package com.goorm.thelastsupper.reservation.repository;

import com.goorm.thelastsupper.reservation.entity.ReservationSlot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationSlotRepository extends JpaRepository<ReservationSlot, String> {
}
