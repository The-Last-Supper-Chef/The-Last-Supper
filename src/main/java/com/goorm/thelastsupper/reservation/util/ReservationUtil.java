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
        if (reservationSlot.getStatus().isNotOpen()) {
            log.warn("슬롯 오픈 상태 아님: slotId={}, 상태={}", reservationSlot.getId(), reservationSlot.getStatus());
            throw new ReservationException(ReservationErrorCode.RESERVATION_SLOT_CLOSED);
        }
        log.info("슬롯 오픈 상태 확인 완료: slotId={}", reservationSlot.getId());
    }

    public static void validateNotAlreadyReserved(ReservationHistoryRepository reservationHistoryRepository, Account account, ReservationSlot reservationSlot) {
        reservationHistoryRepository.findByAccountAndReservationSlot(account, reservationSlot)
                .filter(history -> history.getReservedStatus() == ReservedStatus.CONFIRMED)
                .ifPresent(history -> {
                    log.warn("이미 확정된 예약 존재: accountId={}, slotId={}", account.getId(), reservationSlot.getId());
                    throw new ReservationException(ReservationErrorCode.RESERVATION_DUPLICATE_RESERVATION);
                });

        log.info("중복 예약 없음: accountId={}, slotId={}", account.getId(), reservationSlot.getId());
    }

    public static void validateNotSameDayReservation(ReservationSlot reservationSlot) {
        if (LocalDate.now().isAfter(reservationSlot.getDate().minusDays(1))) {
            log.warn("당일 예약, 취소 불가 조건 위반: 오늘={}, 예약일={}", LocalDate.now(), reservationSlot.getDate());
            throw new ReservationException(ReservationErrorCode.RESERVATION_TIME_EXPIRED);
        }
        log.info("예약일 유효: 오늘={}, 예약일={}", LocalDate.now(), reservationSlot.getDate());
    }

    public static void validateAndUpdateCapacity(ReservationSlot reservationSlot, int totalVisitors) {
        int remainingAfterReservation = reservationSlot.getRemaining() - totalVisitors;

        if (remainingAfterReservation < 0) {
            log.warn("예약 인원 초과: slotId={}, 요청 인원={}, 남은 인원={}",
                    reservationSlot.getId(), totalVisitors, reservationSlot.getRemaining());
            throw new ReservationException(ReservationErrorCode.RESERVATION_CAPACITY_OVER);
        } else if (remainingAfterReservation == 0) {
            reservationSlot.setHold();
            log.info("잔여 인원 0 → HOLD 상태로 변경됨: slotId={}", reservationSlot.getId());
        } else {
            log.info("예약 가능: slotId={}, 요청 인원={}, 남은 인원={}",
                    reservationSlot.getId(), totalVisitors, reservationSlot.getRemaining());
        }
    }

    public static void validateAccountMatch(String historyAccountID, String accountId){
        if(!historyAccountID.equals(accountId)){
            log.warn("id가 일치하지 않습니다. historyAccountID={}, accountId={}", historyAccountID, accountId);
            throw new ReservationException(ReservationErrorCode.RESERVATION_ID_MISMATCH);
        }
        log.info("id가 일치합니다. historyAccountID={}, accountId={}", historyAccountID, accountId);
    }

    public static void validateReservationStatus(ReservationHistory history) {
        if (!history.getReservedStatus().equals(ReservedStatus.CONFIRMED)) {
            log.warn("취소할 예약이 존재하지 않습니다. reservationHistoryId={}", history.getId());
            throw new ReservationException(ReservationErrorCode.RESERVATION_NOT_FOUND);
        }
        log.info("예약 존재 : reservationHistoryId={}", history.getId());
    }
}
