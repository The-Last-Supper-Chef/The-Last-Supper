package com.goorm.thelastsupper.reservation.slot.repository;

import com.goorm.thelastsupper.reservation.slot.entity.ReservationSlot;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

/**
 * 예약 슬롯 조회를 위한 도메인 레이어 인터페이스.
 * 실제 데이터 접근은 인프라 레이어에서 구현됩니다.
 */
public interface SlotReadRepository {

	List<ReservationSlot> findSlotsByPlanIds(List<String> planIds);

	boolean existsBySlotDateTime(LocalDateTime slotDateTime);

	Optional<ReservationSlot> findByDateAndStartTime(LocalDate date, LocalTime time);
}
