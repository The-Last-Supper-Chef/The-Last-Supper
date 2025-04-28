package com.goorm.thelastsupper.reservation.history.repository;

import com.goorm.thelastsupper.account.entity.Account;
import com.goorm.thelastsupper.reservation.history.entity.ReservationHistory;
import com.goorm.thelastsupper.reservation.slot.entity.ReservationSlot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReservationHistoryRepository extends JpaRepository<ReservationHistory, String> {
    Optional<ReservationHistory> findByAccountAndReservationSlot(Account account, ReservationSlot reservationSlot);

    List<ReservationHistory> findAllByAccountAndReservationSlot(Account account, ReservationSlot reservationSlot);

    List<ReservationHistory> findAllByAccount(Account account);
}
