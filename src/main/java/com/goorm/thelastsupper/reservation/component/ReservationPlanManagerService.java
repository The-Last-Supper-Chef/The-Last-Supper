package com.goorm.thelastsupper.reservation.component;

import com.goorm.thelastsupper.reservation.dto.OpenSlotsCommand;
import com.goorm.thelastsupper.reservation.entity.ReservationPlan;
import com.goorm.thelastsupper.reservation.entity.ReservationSlot;
import com.goorm.thelastsupper.reservation.repository.ReservationPlanRepository;
import com.goorm.thelastsupper.restaurant.entity.Restaurant;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * 예약 슬롯 오픈 정책을 검증하는 컴포넌트입니다.
 */
@Component
@RequiredArgsConstructor
public class ReservationPlanManagerService {
	private final SlotPersistenceManagerService slotManagerService;
	private final RestaurantProviderServce restaurantProviderServce;
	private final ReservationPlanRepository planRepo;

	/**
	 * 예약 계획 데이터를 하루씩 나누어서 저장하는 메서드
	 * @param cmd 예약 슬롯 생성 명령 객체
	 * @return 예약 계획
	 */
	public List<ReservationPlan> createAndSave(OpenSlotsCommand cmd) {
		var restaurant = restaurantProviderServce.loadOwner(cmd.ownerId());

		List<ReservationPlan> plans = new ArrayList<>();
		LocalDate currentDate = cmd.startDate();

		// 예약 기간 동안 반복
		Stream.iterate(currentDate, date -> !date.isAfter(cmd.endDate()), date -> date.plusDays(1))
			.filter(date -> !isExceptionDate(date, cmd))  				// 예외 날짜를 제외
			.filter(date -> !shouldSkipDayOfWeek(date, cmd))  			// 해당 요일을 제외
			.forEach(date -> {
				// 예약 계획 생성 및 저장
				ReservationPlan plan = saveReservationPlan(date, cmd, restaurant, plans);
				List<ReservationSlot> slots = plan.createSlots(cmd);  	// 슬롯 생성
				slotManagerService.persist(slots);  // 슬롯 저장
			});

		return plans;  // 생성된 예약 계획 리스트 반환
	}

	/**
	 * 예외 날짜를 체크하는 메서드
	 * @param currentDate 현재 날짜
	 * @param cmd 예약 명령 객체
	 * @return 예외 날짜에 포함되는지 여부
	 */
	private boolean isExceptionDate(LocalDate currentDate, OpenSlotsCommand cmd) {
		return cmd.isExceptionDateEnabled() && cmd.exceptionDates() != null && cmd.exceptionDates().contains(currentDate);
	}

	/**
	 * 요일에 해당하지 않는 날짜를 제외하는 메서드
	 * @param currentDate 현재 날짜
	 * @param cmd 예약 명령 객체
	 * @return 해당 날짜가 제외되어야 하는지 여부
	 */
	private boolean shouldSkipDayOfWeek(LocalDate currentDate, OpenSlotsCommand cmd) {
		return cmd.dayOfWeekBased() && !cmd.repeatDays().contains(currentDate.getDayOfWeek());
	}

	/**
	 * 예약 계획을 생성하고 저장하는 메서드
	 *
	 * @param currentDate 현재 날짜
	 * @param cmd         예약 명령 객체
	 * @param restaurant  레스토랑 객체
	 * @param plans       예약 계획 리스트
	 * @return ReservationPlan 객체
	 */
	private ReservationPlan saveReservationPlan(LocalDate currentDate, OpenSlotsCommand cmd, Restaurant restaurant, List<ReservationPlan> plans) {
		var plan = ReservationPlan.of(restaurant, cmd);
		plan.setPlanDates(currentDate);  // 하루 단위로 날짜 설정
		plans.add(planRepo.save(plan));  // 하루 단위로 저장
		return plan;
	}
}
