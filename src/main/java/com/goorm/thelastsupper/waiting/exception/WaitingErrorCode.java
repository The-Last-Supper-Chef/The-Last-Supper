package com.goorm.thelastsupper.waiting.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum WaitingErrorCode {

    WAITING_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 웨이팅을 찾을 수 없습니다."),
    ALREADY_WAITING(HttpStatus.CONFLICT, "이전 웨이팅이 있습니다."),
    ALREADY_LAST_WAITING(HttpStatus.CONFLICT, "이전 웨이팅이 없습니다."),
    WAITING_NOT_OPEN(HttpStatus.BAD_REQUEST, "아직 오픈되지 않았습니다."),
    ACCOUNT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 고객을 찾을 수 없습니다."),
    WAITING_OPEN_EXIST(HttpStatus.CONFLICT, "대기열이 이미 오픈되어 있습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
