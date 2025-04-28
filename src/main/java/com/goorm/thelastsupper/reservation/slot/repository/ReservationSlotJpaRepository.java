package com.goorm.thelastsupper.reservation.slot.repository;

import com.goorm.thelastsupper.reservation.slot.entity.ReservationSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationSlotJpaRepository extends JpaRepository<ReservationSlot, String> {

	@Query(value = """
      SELECT *
	  FROM reservation_slot rs
      WHERE rs.plan_id IN :planIds
      ORDER BY rs.start_time
      """,
		nativeQuery = true)
	List<ReservationSlot> findSlotsByPlanIds(@Param("planIds") List<String> planIds);
}
