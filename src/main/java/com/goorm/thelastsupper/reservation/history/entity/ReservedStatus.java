package com.goorm.thelastsupper.reservation.history.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReservedStatus {
    CONFIRMED("확정"),
    REJECTED("거절"),
    CANCELED("취소");

    private final String name;
}
