package com.goorm.thelastsupper.waiting.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum WaitingSetCategory {
    OPEN("열림"),
    BLOCK("차단"),
    CLOSE("마감");

    private final String name;
}
