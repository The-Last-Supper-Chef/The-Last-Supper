package com.goorm.thelastsupper.reservation.controller;

import com.goorm.thelastsupper.reservation.dto.ReservationResponse;
import com.goorm.thelastsupper.reservation.service.ReservationQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalTime;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reservation")
public class ReservationQueryController {

    private final ReservationQueryService reservationQueryService;

    @GetMapping("/me")
    public ResponseEntity<ReservationResponse> getMyReservationsByDate(@RequestParam String accountId,
                                                                       @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                                                       @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime time

                                                                            ){
        //Date : yyyy-MM-dd
        //Time : HH:mm:ss
        log.info("예약 취소 요청 수신 - accountId={}, reservedDate={}, reservedTime={}", accountId, date,time);

        return ResponseEntity.ok(reservationQueryService.getMyReservationsByDate(accountId, date, time));
    }
}
