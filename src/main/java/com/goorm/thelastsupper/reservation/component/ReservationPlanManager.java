package com.goorm.thelastsupper.reservation.component;

import com.goorm.thelastsupper.reservation.dto.OpenSlotsCommand;
import com.goorm.thelastsupper.reservation.entity.ReservationPlan;
import com.goorm.thelastsupper.reservation.repository.ReservationPlanRepository;
import com.goorm.thelastsupper.restaurant.entity.Restaurant;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * 예약 슬롯 오픈 정책을 검증하는 컴포넌트입니다.
 */
@Component
@RequiredArgsConstructor
public class ReservationPlanManager {
	private final RestaurantProvider restaurantProvider;
	private final ReservationPlanRepository planRepo;

	/**
	 * 예약 계획 데이터를 하루씩 나누어서 저장하는 메서드
	 * @param cmd 예약 슬롯 생성 명령 객체
	 * @return 예약 계획
	 */
	public List<ReservationPlan> createAndSave(OpenSlotsCommand cmd) {
		var restaurant = restaurantProvider.loadOwner(cmd.ownerId());

		List<ReservationPlan> plans = new ArrayList<>();
		LocalDate currentDate = cmd.startDate();

		// 예약 기간 동안 반복
		while (!currentDate.isAfter(cmd.endDate())) {
			// 예외 날짜가 활성화된 경우 예외 날짜를 제외
			if (isExceptionDate(currentDate, cmd)) {
				currentDate = currentDate.plusDays(1);  // 예외 날짜는 건너뛰고 다음 날짜로 이동
				continue;
			}

			// `dayOfWeekBased`가 true일 경우, `repeatDays`에 해당되지 않는 요일은 제외
			if (shouldSkipDayOfWeek(currentDate, cmd)) {
				currentDate = currentDate.plusDays(1);  // 해당 요일은 건너뛰고 다음 날짜로 이동
				continue;
			}

			// 예약 계획 생성 및 저장
			saveReservationPlan(currentDate, cmd, restaurant, plans);

			currentDate = currentDate.plusDays(1);  // 다음 날로 이동
		}

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
	 * @param currentDate 현재 날짜
	 * @param cmd 예약 명령 객체
	 * @param restaurant 레스토랑 객체
	 * @param plans 예약 계획 리스트
	 */
	private void saveReservationPlan(LocalDate currentDate, OpenSlotsCommand cmd, Restaurant restaurant, List<ReservationPlan> plans) {
		var plan = ReservationPlan.of(restaurant, cmd);
		plan.setPlanDates(currentDate);  // 하루 단위로 날짜 설정
		plans.add(planRepo.save(plan));  // 하루 단위로 저장
	}
}
