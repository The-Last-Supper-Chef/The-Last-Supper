package com.goorm.thelastsupper.waiting.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum WaitingSetCategory {
    OPEN("열림"),
    PAUSE("중단"),
    CLOSE("종료");

    private final String name;
}
