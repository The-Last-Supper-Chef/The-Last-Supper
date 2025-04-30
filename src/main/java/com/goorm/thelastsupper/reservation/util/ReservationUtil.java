package com.goorm.thelastsupper.reservation.util;

import com.goorm.thelastsupper.account.entity.Account;
import com.goorm.thelastsupper.reservation.history.entity.ReservationHistory;
import com.goorm.thelastsupper.reservation.slot.entity.ReservationSlot;
import com.goorm.thelastsupper.reservation.history.entity.ReservedStatus;
import com.goorm.thelastsupper.reservation.common.error.ReservationErrorCode;
import com.goorm.thelastsupper.reservation.common.exception.ReservationException;
import com.goorm.thelastsupper.reservation.history.repository.ReservationHistoryRepository;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

@Slf4j
public class ReservationUtil {

    public static void validateSlotIsOpen(ReservationSlot reservationSlot) {
        reservationSlot.ensureOpen();
    }

    public static void validateNotAlreadyReserved(ReservationHistoryRepository reservationHistoryRepository, Account account, ReservationSlot reservationSlot) {
        reservationHistoryRepository.findByAccountAndReservationSlot(account, reservationSlot)
                .filter(ReservationHistory::isConfirmed)
                .ifPresent(history -> {
                    log.warn("이미 확정된 예약 존재: accountId={}, slotId={}", account.getId(), reservationSlot.getId());
                    throw new ReservationException(ReservationErrorCode.RESERVATION_DUPLICATE_RESERVATION);
                });

        log.info("중복 예약 없음: accountId={}, slotId={}", account.getId(), reservationSlot.getId());
    }

    public static void validateNotAlreadyReserved1(ReservationHistoryRepository reservationHistoryRepository, String accountId, ReservationSlot reservationSlot) {
        reservationHistoryRepository.findByAccountIdAndReservationSlotId(accountId, reservationSlot.getId())
            .filter(ReservationHistory::isConfirmed)
            .ifPresent(history -> {
                log.warn("이미 확정된 예약 존재: accountId={}, slotId={}", accountId, reservationSlot.getId());
                throw new ReservationException(ReservationErrorCode.RESERVATION_DUPLICATE_RESERVATION);
            });
        log.info("중복 예약 없음: accountId={}, slotId={}", accountId, reservationSlot.getId());
    }

    public static void validateNotSameDayReservation(ReservationSlot reservationSlot) {
        reservationSlot.ensureCancellable();
    }

    public static void validateAndUpdateCapacity(ReservationSlot reservationSlot, int totalVisitors) {
        reservationSlot.reserve(totalVisitors);
        log.info("예약 처리 완료: slotId={}, totalVisitors={}", reservationSlot.getId(), totalVisitors);
    }

    public static void validateAccountMatch(String historyAccountID, String accountId){
        if(!historyAccountID.equals(accountId)){
            log.warn("id가 일치하지 않습니다. historyAccountID={}, accountId={}", historyAccountID, accountId);
            throw new ReservationException(ReservationErrorCode.RESERVATION_ID_MISMATCH);
        }
        log.info("id가 일치합니다. historyAccountID={}, accountId={}", historyAccountID, accountId);
    }

    public static void validateReservationStatus(ReservationHistory history) {
        history.ensureConfirmed();
    }
}
