package com.goorm.thelastsupper.account.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {
    CUSTOMER("ROLE_CUSTOMER","고객"),
    OWNER("ROLE_OWNER","점주");

    private final String code;
    private final String name;

}
