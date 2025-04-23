package com.goorm.thelastsupper.account.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {
    CUSTOMER("고객"),
    OWNER("점주");

    private final String name;

}
