package com.goorm.thelastsupper.reservation.history.service;

import com.goorm.thelastsupper.account.entity.Account;
import com.goorm.thelastsupper.account.repository.AccountRepository;
import com.goorm.thelastsupper.reservation.history.dto.ReservationRequest;
import com.goorm.thelastsupper.reservation.history.dto.ReservationResponse;
import com.goorm.thelastsupper.reservation.history.entity.ReservationHistory;
import com.goorm.thelastsupper.reservation.slot.entity.ReservationSlot;
import com.goorm.thelastsupper.reservation.plan.entity.SlotStatus;
import com.goorm.thelastsupper.reservation.common.error.ReservationErrorCode;
import com.goorm.thelastsupper.reservation.common.exception.ReservationException;
import com.goorm.thelastsupper.reservation.history.repository.ReservationHistoryRepository;
import com.goorm.thelastsupper.reservation.slot.repository.JpaSlotReadRepository;
import com.goorm.thelastsupper.reservation.util.EntityFinder;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static com.goorm.thelastsupper.reservation.history.entity.ReservationHistory.createReservation;
import static com.goorm.thelastsupper.reservation.util.ReservationUtil.*;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationService {

    private final JpaSlotReadRepository slotRepository;
    private final ReservationHistoryRepository reservationHistoryRepository;
    private final AccountRepository accountRepository;
    private final EntityFinder entityFinder;

    @Transactional
    public ReservationResponse registerReservation(String accountId, @Valid ReservationRequest request) {

        ReservationSlot reservationSlot = entityFinder.getReservationSlotById(request.slotId());
        log.info("예약 슬롯 조회 성공: slotId={}, date={}, remaining={}", reservationSlot.getId(), reservationSlot.getDate(), reservationSlot.getRemaining());

        // Account account = entityFinder.getAccountById(accountId);
        // log.info("계정 조회 성공: accountId={}, email={}", account.getId(), account.getEmail());

        log.info("슬롯 오픈 상태 검증 시작...");
        validateSlotIsOpen(reservationSlot);

        log.info("중복 예약 여부 검증 시작...");
        // validateNotAlreadyReserved(reservationHistoryRepository, account, reservationSlot);
        validateNotAlreadyReserved1(reservationHistoryRepository, accountId, reservationSlot);

        log.info("당일 예약 불가 검증 시작...");
        validateNotSameDayReservation(reservationSlot);

        log.info("잔여 인원 및 HOLD 상태 검증 시작...");
        validateAndUpdateCapacity(reservationSlot, request.totalVisitors());

        reservationSlot.decreaseRemaining(request.totalVisitors());
        log.info("잔여 인원 차감 완료. slotId={}, 남은 인원={}", reservationSlot.getId(), reservationSlot.getRemaining());

        log.info("예약 등록 시작...");
        // ReservationHistory reservationHistory = createReservation(account,reservationSlot, request.request(), request.totalVisitors());
        ReservationHistory reservationHistory = createReservation(entityFinder.getAccountById1(accountId),reservationSlot, request.request(), request.totalVisitors());

        try {
            ReservationHistory savedHistory = reservationHistoryRepository.save(reservationHistory);
            log.info("예약 등록 성공. slotId={}, accountId={}", savedHistory.getId(), accountId);
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

        // log.info("본인 여부 확인 시작...");
        // validateAccountMatch(reservationHistory.getAccount().getId(), accountId);

        log.info("본인 여부 확인 시작...");
        // FIXME: 추가된 로직 (2025-04-29 이수)
        String savedAccountId = reservationHistoryRepository.findAccountIdByHistoryId(historyId)
            .orElseThrow(() -> new ReservationException(ReservationErrorCode.RESERVATION_ACCOUNT_NOT_FOUND));
        log.info("저장된 계정 ID: {}", savedAccountId);
        if (!savedAccountId.equals(accountId)) {
            throw new ReservationException(ReservationErrorCode.RESERVATION_ACCOUNT_MISMATCH);
        }


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

       // ReservationHistory reservationHistory = entityFinder.getHistoryById(historyId);

        log.info("본인 여부 확인 시작...");
        String optionalAccountId = reservationHistoryRepository.findAccountIdByHistoryId(historyId)
            .orElseThrow(() -> new ReservationException(ReservationErrorCode.RESERVATION_ACCOUNT_NOT_FOUND));
        validateAccountMatch(optionalAccountId, accountId);

        cancelReservation(request.slotId(), historyId, optionalAccountId);

        return registerReservation(accountId, request);
    }

    public ReservationSlot getReservationSlotById(String slotId){
        return slotRepository.findById(slotId)
                .orElseThrow(() -> {
                    log.warn("예약 슬롯을 찾을 수 없습니다. slotId={}", slotId);
                    return new ReservationException(ReservationErrorCode.RESERVATION_SLOT_NOT_FOUND);
                });
    }

    public Account getAccountById(String accountId){
        return accountRepository.findById(accountId)
                .orElseThrow(() -> {
                    String errorMsg = "계정을 찾을 수 없습니다. accountId=" + accountId;
                    log.warn(errorMsg);
                    return new ReservationException(ReservationErrorCode.RESERVATION_NOT_FOUND, errorMsg);
                });
    }

    public ReservationHistory getHistoryById(String historyId){
        return reservationHistoryRepository.findById(historyId)
                .orElseThrow(() -> {
                    log.warn("예약을 찾을 수 없습니다. historyId={}", historyId);
                    return new ReservationException(ReservationErrorCode.RESERVATION_NOT_FOUND);
                });
    }


}
