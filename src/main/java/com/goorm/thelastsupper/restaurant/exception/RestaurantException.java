package com.goorm.thelastsupper.restaurant.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RestaurantException extends RuntimeException{

    private final RestaurantErrorCode errorCode;
}
