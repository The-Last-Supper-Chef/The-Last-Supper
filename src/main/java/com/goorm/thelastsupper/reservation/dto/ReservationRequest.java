package com.goorm.thelastsupper.reservation.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record ReservationRequest(
        @NotNull
        String slotId,

        //Todo : 사용을 안함 -> 엔티티, Response에 추가 필요
        @NotBlank
        String representativeName,

        @NotBlank
        String representativePhone,

        //Todo : 디폴트 false
        boolean isProxyAttendee,

        @Min(1)
        @Max(20)
        int totalVisitors,

        @Length(max = 200)
        String request
) {
}