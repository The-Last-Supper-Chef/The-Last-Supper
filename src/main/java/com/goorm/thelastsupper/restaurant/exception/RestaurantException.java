package com.goorm.thelastsupper.restaurant.exception;

import com.goorm.thelastsupper.common.security.exception.AuthErrorCode;
import com.goorm.thelastsupper.common.security.exception.AuthException;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RestaurantException extends RuntimeException{

    private final RestaurantErrorCode errorCode;

    public static class RestaurantNotFoundException extends RestaurantException {
        public RestaurantNotFoundException() {
            super(RestaurantErrorCode.RESTAURANT_NOT_FOUND);
        }
    }
}
