package com.goorm.thelastsupper.reservation.service;

import org.springframework.stereotype.Service;

import com.goorm.thelastsupper.reservation.dto.OpenSlotsCommand;
import com.goorm.thelastsupper.reservation.dto.OpenSlotsResponse;

@Service
public class OpenSlotsUseCase {
	public OpenSlotsResponse execute(OpenSlotsCommand command) {

		// TODO: 서비스 로직 구현하기
		return new OpenSlotsResponse("planId", 1);
	}
}
