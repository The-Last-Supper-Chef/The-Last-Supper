package com.goorm.thelastsupper.waiting.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record WaitingRequest(
        @NotNull
        @Positive(message = "1 ~ 10사이의 수를 입력할 수 있습니다.")
        @Min(value = 1, message = "1 ~ 10사이의 수를 입력할 수 있습니다.")
        @Max(value = 10, message = "1 ~ 10사이의 수를 입력할 수 있습니다.")
        int headCount ) {
}
