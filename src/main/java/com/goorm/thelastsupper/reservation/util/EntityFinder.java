package com.goorm.thelastsupper.reservation.util;

import com.goorm.thelastsupper.account.entity.Account;
import com.goorm.thelastsupper.account.repository.AccountRepository;
import com.goorm.thelastsupper.reservation.entity.ReservationHistory;
import com.goorm.thelastsupper.reservation.entity.ReservationSlot;
import com.goorm.thelastsupper.reservation.exception.ReservationErrorCode;
import com.goorm.thelastsupper.reservation.exception.ReservationException;
import com.goorm.thelastsupper.reservation.repository.ReservationHistoryRepository;
import com.goorm.thelastsupper.reservation.repository.ReservationSlotRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EntityFinder {

    private final ReservationSlotRepository reservationSlotRepository;
    private final ReservationHistoryRepository reservationHistoryRepository;
    private final AccountRepository accountRepository;

    public ReservationSlot getReservationSlotById(String slotId){
        return reservationSlotRepository.findById(slotId)
                .orElseThrow(() -> {
                    log.warn("예약 슬롯을 찾을 수 없습니다. slotId={}", slotId);
                    return new ReservationException(ReservationErrorCode.RESERVATION_SLOT_NOT_FOUND);
                });
    }

    public Account getAccountById(String accountId){
        return accountRepository.findById(accountId)
                .orElseThrow(() -> {
                    log.warn("계정을 찾을 수 없습니다. accountId={}", accountId);
                    return new ReservationException(ReservationErrorCode.RESERVATION_NOT_FOUND);
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
