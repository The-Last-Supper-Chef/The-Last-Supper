package com.goorm.thelastsupper.reservation.history.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record ReservationRequest(
        @NotNull
        String slotId,

        String representativeName,

        String representativePhone,

        boolean isProxyAttendee,

        @Min(1)
        @Max(20)
        int totalVisitors,

        @Length(max = 200)
        String request
) {
}
