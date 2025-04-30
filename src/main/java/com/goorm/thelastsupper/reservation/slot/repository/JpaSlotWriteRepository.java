package com.goorm.thelastsupper.reservation.slot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.goorm.thelastsupper.reservation.slot.entity.ReservationSlot;

/**
 * 실제 데이터베이스와 상호작용하는 JPA 리포지토리입니다.
 * SlotRepository 인터페이스를 구현합니다.
 */
public interface JpaSlotWriteRepository
	extends JpaRepository<ReservationSlot, String>, SlotWriteRepository {
}
