package com.goorm.thelastsupper.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    INVALID_INPUT_PARAMETER(HttpStatus.BAD_REQUEST, "입력값이 맞지 않습니다."),
    INVALID_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류입니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
