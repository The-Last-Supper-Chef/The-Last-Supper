package com.goorm.thelastsupper.reservation.history.dto;

import com.goorm.thelastsupper.reservation.history.entity.RejectionReason;
import com.goorm.thelastsupper.reservation.history.entity.ReservationHistory;
import com.goorm.thelastsupper.reservation.history.entity.ReservedStatus;

public record ReservationResponse(
        String accountId,
        String slotId,
        String request,
        ReservedStatus status,
        RejectionReason rejectionReason,
        long reservedPeople,
        boolean visible
) {
}
