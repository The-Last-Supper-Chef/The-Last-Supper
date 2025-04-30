package com.goorm.thelastsupper.account.entity;

import java.util.Arrays;

import com.goorm.thelastsupper.common.security.exception.AuthException;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {
    CUSTOMER("ROLE_CUSTOMER","고객"),
    OWNER("ROLE_OWNER","점주");

    private final String code;
    private final String name;

    public static Role of(String role) {
        return Arrays.stream(Role.values())
            .filter(r -> r.getCode().equals(role))
            .findFirst()
            .orElseThrow(AuthException.InvalidClaimValueException::new);
    }

}
