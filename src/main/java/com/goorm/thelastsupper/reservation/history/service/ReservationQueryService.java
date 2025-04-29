package com.goorm.thelastsupper.reservation.history.service;

import com.goorm.thelastsupper.account.entity.Account;

import com.goorm.thelastsupper.reservation.common.error.ReservationErrorCode;
import com.goorm.thelastsupper.reservation.common.exception.ReservationException;
import com.goorm.thelastsupper.reservation.history.dto.ReservationResponse;
import com.goorm.thelastsupper.reservation.history.entity.ReservationHistory;
import com.goorm.thelastsupper.reservation.history.entity.ReservedStatus;
import com.goorm.thelastsupper.reservation.history.repository.ReservationHistoryRepository;
import com.goorm.thelastsupper.reservation.slot.entity.ReservationSlot;
import com.goorm.thelastsupper.reservation.slot.repository.JpaSlotReadRepository;
import com.goorm.thelastsupper.reservation.util.EntityFinder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationQueryService {

    private final EntityFinder entityFinder;
    private final ReservationHistoryRepository reservationHistoryRepository;
    private final JpaSlotReadRepository reservationSlotRepository;

    public ReservationResponse getMyReservationsByDate(String accountId, LocalDate date, LocalTime time) {


        Account account = entityFinder.getAccountById1(accountId);
        log.info("계정 조회 성공: accountId={}, email={}", account.getId(), account.getEmail());

        ReservationSlot reservationSlot = reservationSlotRepository.findByDateAndStartTime(date, time)
                .orElseThrow(() -> {
                    log.warn("Slot이 존재하지 않음 : date={}, time={}", date, time);
                    throw new ReservationException(ReservationErrorCode.RESERVATION_SLOT_NOT_FOUND);
                });
        log.info("예약 슬롯 조회 성공: date={}, time={}, slotId={}", date, time, reservationSlot.getId());

        List<ReservationHistory> reservationHistories = reservationHistoryRepository.findAllByAccountAndReservationSlot(account, reservationSlot);
        log.info("예약 히스토리 수 조회: count={}", reservationHistories.size());

        for (ReservationHistory reservationHistory : reservationHistories) {
            if(reservationHistory.getReservedStatus().equals(ReservedStatus.CONFIRMED)){
                log.info("확정된 예약 발견: reservationHistoryId={}, status={}", reservationHistory.getId(), reservationHistory.getReservedStatus());
                return ReservationResponse.mapFromHistory(reservationHistory);
            }
        }

        log.warn("확정된 예약을 찾지 못함: accountId={}, date={}, time={}", accountId, date, time);
        throw new ReservationException(ReservationErrorCode.RESERVATION_NOT_FOUND);
    }

    public List<ReservationResponse> getAllMyReservation(String accountId) {
        Account account = entityFinder.getAccountById1(accountId);
        log.info("계정 조회 성공: accountId={}, email={}", account.getId(), account.getEmail());

        List<ReservationHistory> reservationHistory = reservationHistoryRepository.findAllByAccount(account);

        return reservationHistory.stream()
                .map(ReservationResponse::mapFromHistory)
                .toList();
    }
}
