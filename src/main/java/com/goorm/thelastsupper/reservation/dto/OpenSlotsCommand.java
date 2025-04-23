package com.goorm.thelastsupper.reservation.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Singular;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * 예약 슬롯 오픈 요청 DTO (record + Bean Validation).
 *
 */
@Builder
public record OpenSlotsCommand(

	/* ----- 필수 필드 ----- */
	@NotNull @Future(message = "시작일은 오늘 이후여야 합니다.")
	LocalDate startDate,

	@NotNull(message = "영업 시작 시간은 필수입니다.")
	LocalTime openTime,

	@NotNull(message = "영업 종료 시간은 필수입니다.")
	LocalTime closeTime,

	@Singular("breakTime")
	@Valid
	@Size(max = 5, message = "휴식 구간은 최대 5개까지만 등록할 수 있습니다.")
	List<BreakInterval> breakTimes,

	@NotNull @Positive(message = "슬롯 길이는 양수여야 합니다.")
	Duration slotDuration,

	@Min(1) @Max(50)
	int capacity
) {

	/* ----- 추가 도메인 규칙 ----- */

	@AssertTrue(message = "영업 시작 시간은 종료 시간보다 빨라야 합니다.")
	private boolean isOpenBeforeClose() {
		return openTime == null || closeTime == null || openTime.isBefore(closeTime);
	}

	/* ----- 휴식 구간 값 객체 ----- */

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
