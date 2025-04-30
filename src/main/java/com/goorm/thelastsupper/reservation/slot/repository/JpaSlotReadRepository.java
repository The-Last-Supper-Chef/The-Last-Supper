package com.goorm.thelastsupper.reservation.slot.repository;

import com.goorm.thelastsupper.reservation.slot.entity.ReservationSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

/**
 * 실제 데이터베이스와 상호작용하는 JPA 리포지토리입니다.
 * SlotRepository 인터페이스를 구현합니다.
 */
public interface JpaSlotReadRepository extends JpaRepository<ReservationSlot, String>, SlotReadRepository {

	/**
	 * reservation_slot 테이블에서 주어진 예약 계획 ID 목록에 맞는 슬롯을 조회합니다.
	 */
	@Query(value = """
      SELECT rs
        FROM ReservationSlot rs
       WHERE rs.plan.id IN :planIds
      ORDER BY rs.startTime
      """)
	@Override
	List<ReservationSlot> findSlotsByPlanIds(@Param("planIds") List<String> planIds);

	@Override
	boolean existsBySlotDateTime(LocalDateTime slotDateTime);

	@Override
	Optional<ReservationSlot> findByDateAndStartTime(LocalDate date, LocalTime time);
}
