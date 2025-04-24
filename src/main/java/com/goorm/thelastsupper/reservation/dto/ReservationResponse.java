package com.goorm.thelastsupper.reservation.dto;

import com.goorm.thelastsupper.reservation.entity.RejectionReason;
import com.goorm.thelastsupper.reservation.entity.ReservationHistory;
import com.goorm.thelastsupper.reservation.entity.ReservedStatus;

public record ReservationResponse(
        String accountId,
        String slotId,
        String request,
        ReservedStatus status,
        RejectionReason rejectionReason,
        long reservedPeople,
        boolean visible
) {
    public static ReservationResponse mapFromHistory(ReservationHistory savedHistory){
        return new ReservationResponse(
                savedHistory.getAccount().getId(),
                savedHistory.getReservationSlot().getId(),
                savedHistory.getRequest(),
                savedHistory.getReservedStatus(),
                savedHistory.getRejectionReason(),
                savedHistory.getReservedPeople(),
                savedHistory.isVisible());
    }

}
