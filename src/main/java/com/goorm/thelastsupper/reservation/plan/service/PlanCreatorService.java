package com.goorm.thelastsupper.reservation.plan.service;

import com.goorm.thelastsupper.reservation.common.error.ReservationErrorCode;
import com.goorm.thelastsupper.reservation.common.exception.ReservationException;
import com.goorm.thelastsupper.reservation.plan.component.PlanReadService;
import com.goorm.thelastsupper.reservation.plan.component.PlanWriteService;
import com.goorm.thelastsupper.reservation.plan.dto.OpenSlotsCommandRequest;
import com.goorm.thelastsupper.reservation.plan.entity.ReservationPlan;
import com.goorm.thelastsupper.restaurant.service.RestaurantQueryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * 예약 슬롯 오픈 정책을 검증하는 컴포넌트입니다.
 * - 결합: RestaurantQueryService, PlanReadService, PlanWriteService
 * - 응집: 예약 계획 생성 및 저장
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PlanCreatorService {
	private final RestaurantQueryService restaurantQueryService;
	private final PlanReadService reservationPlanReadService;
	private final PlanWriteService reservationPlanWriteService;

	// ============================== public ============================== //
	/**
	 * 예약 계획 데이터를 하루씩 나누어서 저장하는 메서드
	 * @param cmd 예약 슬롯 생성 명령 객체
	 * @return 예약 계획
	 */
	public List<ReservationPlan> openPlans(OpenSlotsCommandRequest cmd) {
		List<ReservationPlan> plans = new ArrayList<>();
		iterateDatesAndPersist(cmd, plans);
		return plans;
	}

	// ============================== private ============================== //

	private boolean existsPlan(String ownerId, LocalDate date) {
		boolean exists = reservationPlanReadService.existsByRestaurantIdAndPlanDate(ownerId, date);
		if (exists) {
			throw new ReservationException(ReservationErrorCode.RESERVATION_PLAN_ALREADY_EXISTS,"예약 일정이 이미 존재합니다.");
		}
		return exists;
	}

	/** 예약 기간을 순회하며 "예약 일정"을 생성·저장 */
	private void iterateDatesAndPersist(OpenSlotsCommandRequest cmd, List<ReservationPlan> plans) {
		Stream.iterate(cmd.startDate(), d -> !d.isAfter(cmd.endDate()), d -> d.plusDays(1))
			.filter(d -> !shouldSkipDayOfWeek(d, cmd))			// 예약 요일 체크
			.filter(d -> !existsPlan(cmd.ownerId(), d))			// 기존 예약 일정 체크
			.forEach(d -> saveReservationPlan(d, cmd, plans));	// 예약 계획 저장
	}

	/**
	 * 예약 계획을 생성하고 저장하는 메서드
	 *
	 * @param currentDate 현재 날짜
	 * @param cmd         예약 명령 객체
	 * @param plans       예약 계획 리스트
	 */
	public void saveReservationPlan(
		LocalDate currentDate, OpenSlotsCommandRequest cmd, List<ReservationPlan> plans) {
		var plan = ReservationPlan.of(restaurantQueryService.getReferenceById(cmd.ownerId()), cmd);
		plan.setPlanDates(currentDate);  					// 하루 단위로 날짜 설정
		plans.add(reservationPlanWriteService.save(plan));  // 하루 단위로 저장
	}

	/**
	 * 예외 날짜를 체크하는 메서드
	 * @param currentDate 현재 날짜
	 * @param cmd 예약 명령 객체
	 * @return 예외 날짜에 포함되는지 여부
	 */
	private boolean isExceptionDate(LocalDate currentDate, OpenSlotsCommandRequest cmd) {
		return cmd.isExceptionDateEnabled() && cmd.exceptionDates() != null && cmd.exceptionDates().contains(currentDate);
	}

	/**
	 * 요일에 해당하지 않는 날짜를 제외하는 메서드
	 * @param currentDate 현재 날짜
	 * @param cmd 예약 명령 객체
	 * @return 해당 날짜가 제외되어야 하는지 여부
	 */
	private boolean shouldSkipDayOfWeek(LocalDate currentDate, OpenSlotsCommandRequest cmd) {
		return cmd.dayOfWeekBased() && !cmd.repeatDays().contains(currentDate.getDayOfWeek());
	}
}
