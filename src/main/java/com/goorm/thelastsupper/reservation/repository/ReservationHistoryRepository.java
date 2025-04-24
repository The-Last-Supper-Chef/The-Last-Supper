package com.goorm.thelastsupper.reservation.repository;

import com.goorm.thelastsupper.account.entity.Account;
import com.goorm.thelastsupper.reservation.entity.ReservationHistory;
import com.goorm.thelastsupper.reservation.entity.ReservationSlot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReservationHistoryRepository extends JpaRepository<ReservationHistory, String> {
    Optional<ReservationHistory> findByAccountAndReservationSlot(Account account, ReservationSlot reservationSlot);
}
