package com.goorm.thelastsupper.reservation.plan.repository;

import com.goorm.thelastsupper.reservation.plan.entity.ReservationPlan;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * 예약 Plan + Slot 조회 전용 Spring-Data JPA Repository.
 *
 * • <b>fetch join + distinct</b>로 Plan·Slot·Restaurant 을 한 번에 적재해 N+1 제거
 * • <b>기간 겹침</b> 조건(p.startDate ≤ :end AND p.endDate ≥ :start)으로
 *   다일짜 Plan·기간 Plan 모두 커버
 */
public interface PlanReadJpaRepository extends JpaRepository<ReservationPlan, String> {
	/**
	 * reservation_plan 테이블에서 ID 목록만 꺼냅니다.
	 */
	@Query(value = """
      SELECT p.id
        FROM reservation_plan p
       WHERE p.restaurant_id = :restaurantId
         AND p.plan_date BETWEEN :start AND :end
      """,
		nativeQuery = true)
	List<String> findPlanIdsByRestaurantAndDateRange(
		@Param("restaurantId") String restaurantId,
		@Param("start")        LocalDate start,
		@Param("end")          LocalDate end
	);

	/**
	 * 위에서 가져온 ID 목록으로 ReservationPlan+slots 을 한 번에 로딩합니다.
	 */
	@EntityGraph(attributePaths = {"slots"})
	@Query("""
    SELECT p
      FROM ReservationPlan p
     WHERE p.id IN :ids
    """)
	List<ReservationPlan> findAllWithSlotsByIds(@Param("ids") List<String> ids);
	/**
	 * [단일 날짜] Plan (+ Slot) 조회 (옵션).
	 * <p>하루짜리 Plan 을 ‘날짜 == startDate == endDate’ 로 저장하는 경우 사용</p>
	 */
	@Query("""
        select distinct p
        from ReservationPlan p
        join fetch p.slots s
        where p.restaurant.id = :restaurantId
          and p.planDate      = :date
        order by s.startTime
        """)
	List<ReservationPlan> findWithSlotsByRestaurantAndDate(
		@Param("restaurantId") String restaurantId,
		@Param("date")         LocalDate date);



	@Query(value = """
    select case when count(*)>0 then true else false end
	from reservation_plan p
    where restaurant_id = :restaurantId
      and plan_date     = :planDate
    """, nativeQuery = true)
	boolean existsPlan(
		String restaurantId, LocalDate planDate);

	boolean existsByRestaurantIdAndPlanDate(String restaurantId, LocalDate planDate);
}
