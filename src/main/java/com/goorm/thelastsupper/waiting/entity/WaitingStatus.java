package com.goorm.thelastsupper.waiting.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum WaitingStatus {
    WAITING("대기중"),
    CANCEL("취소"),
    SUCCESS("완료"),
    DELAY("딜레이");

    private final String name;
}
