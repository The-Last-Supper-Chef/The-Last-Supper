package com.goorm.thelastsupper.reservation.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Singular;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * 예약 슬롯 오픈 요청 DTO (record + Bean Validation).
 *
 * <p>
 * 점주가 기간·요일·타임슬롯 방식 중 하나(또는 조합)로 예약 슬롯을 개설할 때 사용하는 명령 객체입니다.
 * 필드는 모두 불변이며, Spring MVC가 Bean Validation 규칙을 자동 적용합니다.
 * </p>
 */
@Builder
public record OpenSlotsCommand(

	/* ---------- 메타 정보 ---------- */
	Long ownerId,                           // 점주 ID
	boolean dateBased,                     // 일자 작성 여부
	boolean dayOfWeekBased,                // 요일 작성 여부
	boolean timeSlotBased,                 // 타임슬롯 작성 여부

	/* ---------- 기간 & 예외 ---------- */

	@NotNull @FutureOrPresent(message = "시작일은 오늘 이후여야 합니다.")
	LocalDate startDate,

	@NotNull @Future(message = "종료일은 시작일 이후여야 합니다.")
	LocalDate endDate,

	@Singular("exceptionDate")
	@Size(max = 10, message = "예외 선택 일자는 최다 10개까지 등록할 수 있습니다.")
	List<@Future(message = "예외 일자는 미래 날짜여야 합니다.") LocalDate> exceptionDates,

	/* ---------- 반복 요일 ---------- */

	@Singular("repeatDay")
	@Size(max = 7, message = "반복 요일은 최대 7개까지 지정할 수 있습니다.")
	List<DayOfWeek> repeatDays,

	/* ---------- 시간표 ---------- */

	@NotNull(message = "오픈 시간은 필수입니다.")
	LocalTime openTime,

	@NotNull(message = "종료 시간은 필수입니다.")
	LocalTime closeTime,

	@Singular("breakTime")
	@Valid
	@Size(max = 1, message = "휴식 구간은 최대 1개까지만 등록할 수 있습니다.")
	List<BreakInterval> breakTimes,

	/* ---------- 슬롯 설정 ---------- */

	@NotNull(message = "턴 시간은 필수입니다.")                 // 기본 30 분 권장
	@Positive(message = "턴 시간은 양수여야 합니다.")
	Duration slotDuration,

	@Min(value = 1,  message = "입장 인원은 1명 이상이어야 합니다.")
	@Max(value = 50, message = "입장 인원은 50명을 초과할 수 없습니다.")
	int capacity
) {

	/* ---------- 추가 도메인 규칙 ---------- */

	/** 오픈 시간 &lt; 종료 시간을 보장한다. */
	@AssertTrue(message = "오픈 시간은 종료 시간보다 빨라야 합니다.")
	private boolean isOpenBeforeClose() {
		return openTime == null || closeTime == null || openTime.isBefore(closeTime);
	}

	/** 시작일 &lt;= 종료일을 보장한다. */
	@AssertTrue(message = "시작일은 종료일과 같거나 이전이어야 합니다.")
	private boolean isStartBeforeEnd() {
		return startDate == null || endDate == null || !startDate.isAfter(endDate);
	}

	/* ---------- 휴식 구간 값 객체 ---------- */

	/**
	 * 휴식 시간 구간.<br>
	 * <code>breakStart &lt; breakEnd</code> 규칙을 @AssertTrue로 검증한다.
	 */
	public record BreakInterval(
		@NotNull LocalTime breakStart,
		@NotNull LocalTime breakEnd
	) {
		@AssertTrue(message = "휴식 시작 시간은 종료 시간보다 빨라야 합니다.")
		private boolean isStartBeforeEnd() {
			return breakStart == null || breakEnd == null || breakStart.isBefore(breakEnd);
		}
	}
}
