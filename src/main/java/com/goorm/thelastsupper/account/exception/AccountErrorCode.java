package com.goorm.thelastsupper.account.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AccountErrorCode {
    ACCOUNT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 엔티티를 찾을 수 없습니다."),
    EMAIL_DUPLICATION(HttpStatus.BAD_REQUEST, "이미 사용 중인 이메일입니다."),
    ACCOUNT_WITHDRAWN(HttpStatus.FORBIDDEN, "탈퇴한 회원입니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
