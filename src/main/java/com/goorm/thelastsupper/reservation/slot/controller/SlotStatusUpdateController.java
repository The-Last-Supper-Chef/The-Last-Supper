package com.goorm.thelastsupper.reservation.slot.controller;

import java.util.Collections;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.goorm.thelastsupper.reservation.common.exception.ReservationException;
import com.goorm.thelastsupper.reservation.slot.usecase.SlotStatusUsecase;
import com.goorm.thelastsupper.restaurant.exception.ApiResponse;
import com.goorm.thelastsupper.reservation.slot.dto.SlotStatusRequest;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/reservations/restaurants/slots")
@AllArgsConstructor
public class SlotStatusUpdateController {
	private final SlotStatusUsecase slotStatusUsecase;

	/**
	 * 슬롯 상태를 변경하는 메서드
	 * @param slotId    슬롯 ID
	 * @param request   상태 변경 요청 (상태 값 포함)
	 * @return          ResponseEntity 객체
	 */
	@PutMapping("/{slotId}/status")
	public ResponseEntity<?> changeSlotStatus(
		@PathVariable("slotId") String slotId,
		@RequestBody SlotStatusRequest request) {

		try {
			// 상태 변경 로직 실행
			slotStatusUsecase.execute(slotId, request.getSlotStatus());

			// 성공 응답 반환
			return ResponseEntity.ok(ApiResponse.success(Collections.singletonList(slotId), "슬롯 상태가 성공적으로 변경되었습니다."));
		} catch (ReservationException e) {
			return ResponseEntity.badRequest()
				.body(ApiResponse.error("잘못된 슬롯 상태", Collections.singletonList(e.getMessage()), org.springframework.http.HttpStatus.BAD_REQUEST));
		} catch (Exception e) {
			return ResponseEntity.status(500)
				.body(ApiResponse.error("슬롯 상태 변경 오류", Collections.singletonList(e.getMessage()), org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR));
		}
	}
}
