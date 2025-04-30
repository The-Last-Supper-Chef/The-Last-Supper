package com.goorm.thelastsupper.notofication.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NotificationCategory {
    WAITING("웨이팅"),
    RESERVATION("예약");

    private final String name;

}