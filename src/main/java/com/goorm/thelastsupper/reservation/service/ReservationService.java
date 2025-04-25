package com.goorm.thelastsupper.reservation.service;

import com.goorm.thelastsupper.account.entity.Account;
import com.goorm.thelastsupper.account.repository.AccountRepository;
import com.goorm.thelastsupper.reservation.dto.ReservationRequest;
import com.goorm.thelastsupper.reservation.dto.ReservationResponse;
import com.goorm.thelastsupper.reservation.entity.ReservationHistory;
import com.goorm.thelastsupper.reservation.entity.ReservationSlot;
import com.goorm.thelastsupper.reservation.entity.SlotStatus;
import com.goorm.thelastsupper.reservation.exception.ReservationErrorCode;
import com.goorm.thelastsupper.reservation.exception.ReservationException;
import com.goorm.thelastsupper.reservation.repository.ReservationHistoryRepository;
import com.goorm.thelastsupper.reservation.repository.ReservationSlotRepository;
import com.goorm.thelastsupper.reservation.util.EntityFinder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static com.goorm.thelastsupper.reservation.entity.ReservationHistory.createReservation;
import static com.goorm.thelastsupper.reservation.util.ReservationUtil.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationHistoryRepository reservationHistoryRepository;
    private final EntityFinder entityFinder;

    @Transactional
    public ReservationResponse registerReservation(String accountId, @Valid ReservationRequest request) {

        ReservationSlot reservationSlot = entityFinder.getReservationSlotById(request.slotId());
        log.info("예약 슬롯 조회 성공: slotId={}, date={}, remaining={}", reservationSlot.getId(), reservationSlot.getDate(), reservationSlot.getRemaining());

        Account account = entityFinder.getAccountById(accountId);
        log.info("계정 조회 성공: accountId={}, email={}", account.getId(), account.getEmail());

        log.info("슬롯 오픈 상태 검증 시작...");
        validateSlotIsOpen(reservationSlot);

        log.info("중복 예약 여부 검증 시작...");
        validateNotAlreadyReserved(reservationHistoryRepository, account, reservationSlot);

        log.info("당일 예약 불가 검증 시작...");
        validateNotSameDayReservation(reservationSlot);

        log.info("잔여 인원 및 HOLD 상태 검증 시작...");
        validateAndUpdateCapacity(reservationSlot, request.totalVisitors());

        reservationSlot.decreaseRemaining(request.totalVisitors());
        log.info("잔여 인원 차감 완료. slotId={}, 남은 인원={}", reservationSlot.getId(), reservationSlot.getRemaining());

        log.info("예약 등록 시작...");
        ReservationHistory reservationHistory = createReservation(account,reservationSlot, request.request(), request.totalVisitors());

        try {
            ReservationHistory savedHistory = reservationHistoryRepository.save(reservationHistory);
            log.info("예약 등록 성공. reservationId={}, accountId={}", savedHistory.getId(), account.getId());
            return ReservationResponse.mapFromHistory(savedHistory);
        } catch (Exception e) {
            log.info("예약 저장 실패. slotId={}, accountId={}, error={}", reservationSlot.getId(), accountId, e.getMessage(), e);
            throw new ReservationException(ReservationErrorCode.RESERVATION_INTERNAL_ERROR);
        }
    }

    @Transactional
    public void cancelReservation(String slotId, String historyId, String accountId) {

        ReservationSlot reservationSlot = entityFinder.getReservationSlotById(slotId);
        log.info("예약 슬롯 조회 성공: slotId={}, date={}, remaining={}", reservationSlot.getId(), reservationSlot.getDate(), reservationSlot.getRemaining());

        log.info("예약 존재 여부 확인 시작...");
        ReservationHistory reservationHistory = entityFinder.getHistoryById(historyId);
        validateReservationStatus(reservationHistory);

        log.info("본인 여부 확인 시작...");
        validateAccountMatch(reservationHistory.getAccount().getId(), accountId);

        log.info("당일 취소 불가 검증 시작...");
        validateNotSameDayReservation(reservationSlot);

        log.info("잔여 인원 및 OPEN 상태 검증 시작...");
        reservationSlot.increaseRemaining(reservationHistory.getReservedPeople());
        if(reservationSlot.getRemaining() > 0 && reservationSlot.getStatus().equals(SlotStatus.HOLD)){
            reservationSlot.setOpen();
        }
        log.info("잔여 인원 증가 완료. slotId={}, 남은 인원={}, slot 상태={}", reservationSlot.getId(), reservationSlot.getRemaining(), reservationSlot.getStatus());

        log.info("예약 취소 시작...");
        reservationHistory.setCancel();
        log.info("예약 취소 완료...");
    }

    @Transactional
    public ReservationResponse modifyReservation(String historyId, String accountId, ReservationRequest request) {

        ReservationHistory reservationHistory = entityFinder.getHistoryById(historyId);

        String historyAccountID = reservationHistory.getAccount().getId();

        log.info("본인 여부 확인 시작...");
        validateAccountMatch(reservationHistory.getAccount().getId(), accountId);

        cancelReservation(request.slotId(), historyId, historyAccountID);

        return registerReservation(accountId, request);
    }

}
