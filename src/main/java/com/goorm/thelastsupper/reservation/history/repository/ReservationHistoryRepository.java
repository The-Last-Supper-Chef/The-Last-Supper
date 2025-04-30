package com.goorm.thelastsupper.reservation.history.repository;

import com.goorm.thelastsupper.account.entity.Account;
import com.goorm.thelastsupper.reservation.history.entity.ReservationHistory;
import com.goorm.thelastsupper.reservation.slot.entity.ReservationSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReservationHistoryRepository extends JpaRepository<ReservationHistory, String> {
    Optional<ReservationHistory> findByAccountAndReservationSlot(Account account, ReservationSlot reservationSlot);

    // Optional<ReservationHistory> findByAccountIdAndReservationSlot(String accountId, ReservationSlot reservationSlot);
    @Query(value = """
      SELECT *
        FROM reservation_history rh
       WHERE rh.account_id            = :accountId
         AND rh.reservation_slot_id   = :slotId
      """,
        nativeQuery = true)
    Optional<ReservationHistory> findByAccountIdAndReservationSlotId(
        @Param("accountId") String accountId,
        @Param("slotId")    String slotId
    );

    List<ReservationHistory> findAllByAccountAndReservationSlot(Account account, ReservationSlot reservationSlot);

    List<ReservationHistory> findAllByAccount(Account account);

    @Query(value = "SELECT rh.account_id "
        + "FROM reservation_history rh "
        + "WHERE rh.id = :historyId",
        nativeQuery = true)
    Optional<String> findAccountIdByHistoryId(@Param("historyId") String historyId);

}
