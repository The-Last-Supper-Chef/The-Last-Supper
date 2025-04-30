package com.goorm.thelastsupper.reservation.slot.repository;

import com.goorm.thelastsupper.reservation.slot.entity.ReservationSlot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationTimeSlotRepository extends JpaRepository<ReservationSlot, String> {
}
