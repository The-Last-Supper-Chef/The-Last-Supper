package com.goorm.thelastsupper.reservation.slot.repository;

import java.time.LocalDateTime;

import com.goorm.thelastsupper.reservation.slot.entity.ReservationSlot;
import com.goorm.thelastsupper.reservation.common.error.ReservationErrorCode;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 슬롯 저장소 Port.
 *
 * <p>saveAll + flush 로 대량 저장하고,
 * UNIQUE 제약·낙관적 락 등 <strong>중복 시간대</strong> 오류를
 */
public interface ReservationTimeSlotRepository
	extends JpaRepository<ReservationSlot, String> {

	/**
	 * slot_datetime으로 예약 슬롯이 이미 존재하는지 확인
	 * @param slotDateTime 중복 확인을 위한 slot_datetime
	 * @return 존재하면 true, 존재하지 않으면 false
	 */
	boolean existsBySlotDateTime(LocalDateTime slotDateTime);
}
