package com.goorm.thelastsupper.reservation.history.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RejectionReason {
    STORE_ISSUE("매장사정"),        // 매장사정
    INGREDIENT_SHORTAGE("식재료부족"), // 식재료부족
    SCHEDULE_CHANGE("일정변경"),    // 일정변경
    PERSONAL_REASON("개인사정");     // 개인사정

    private final String name;
}
