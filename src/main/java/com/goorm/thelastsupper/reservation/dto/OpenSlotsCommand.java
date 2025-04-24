package com.goorm.thelastsupper.reservation.dto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Singular;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * 예약 슬롯 오픈 요청 DTO (record + Bean Validation).
 * 점주가 기간·요일·타임슬롯 방식 중 하나(또는 조합)로 예약 슬롯을 개설할 때 사용하는 명령 객체입니다.
 */
@Builder(toBuilder = true)
public record OpenSlotsCommand(
	String ownerId,

	/* ---------- 기간 및 예외 ---------- */
	@NotNull(message = "예약 시작 기간(startDate)는 필수 입력 값입니다.")
	@FutureOrPresent(message = "시작일은 오늘 이후여야 합니다.")
	LocalDate startDate,

	@NotNull(message = "예약 종료 기간(endDate)는 필수 입력 값입니다.")
	@Future(message = "종료일은 시작일 이후여야 합니다.")
	LocalDate endDate,
	@NotNull(message = "예약 일자 선택 여부(isExceptionDateEnabled)는 필수 입력 값입니다.")
	boolean isExceptionDateEnabled,
	@Singular("exceptionDate")
	@Size(max = 10, message = "예외 선택 일자는 최다 10개까지 등록할 수 있습니다.")
	List<@Future(message = "예외 일자는 미래 날짜여야 합니다.") LocalDate> exceptionDates,

	/* ---------- 요일 설정 ---------- */
	@NotNull(message = "요일 선택 여부(dayOfWeekBased)는 필수 입력 값입니다.")
	boolean dayOfWeekBased,
	@Singular("repeatDay")
	@Size(max = 7, message = "반복 요일은 최대 7개까지 지정할 수 있습니다.")
	List<DayOfWeek> repeatDays,

	/* ---------- 시간대 설정 ---------- */
	@NotNull(message = "영업 시작 시간(openTime)은 필수 입력 값입니다.")
	LocalTime openTime,

	@NotNull(message = "영업 종료 시간(closeTime)은 필수 입력 값입니다.")
	LocalTime closeTime,

	@Singular("breakTime")
	@Valid
	@Size(max = 1, message = "휴식 구간은 최대 1개까지만 등록할 수 있습니다.")
	List<BreakInterval> breakTimes,

	/* ---------- 슬롯 설정 ---------- */

	@NotNull(message = "슬롯 단위(slotDuration)는 필수 입력 값입니다.")
	@Min(value = 1, message = "슬롯 단위는 1분 이상이어야 합니다.")
	@Max(value = 120, message = "슬롯 단위는 120분을 초과할 수 없습니다.")
	Long slotDuration,

	@NotNull(message = "슬롯 단위(slotDuration)는 필수 입력 값입니다.")
	@Min(value = 1,  message = "입장 인원은 1명 이상이어야 합니다.")
	@Max(value = 50, message = "입장 인원은 50명을 초과할 수 없습니다.")
	int capacity
) { }
