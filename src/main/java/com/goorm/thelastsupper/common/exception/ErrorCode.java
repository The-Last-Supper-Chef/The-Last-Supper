package com.goorm.thelastsupper.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import org.springframework.boot.logging.LogLevel;

@Getter
public enum ErrorCode {

    // 테스트 에러
    TEST(HttpStatus.INTERNAL_SERVER_ERROR, "001", "비즈니스 예외 테스트입니다.", LogLevel.ERROR),

    // 공통 에러
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "E-500", "예기치 않은 오류가 발생했습니다.", LogLevel.ERROR),
    DUPLICATE_RESOURCE(HttpStatus.CONFLICT, "R-001", "이미 존재하는 리소스입니다.", LogLevel.WARN),
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "C-002", "파라미터가 유효하지 않습니다.", LogLevel.WARN),
    BODY_NOT_READABLE(HttpStatus.BAD_REQUEST, "C-003", "요청 바디가 누락되었거나 형식이 올바르지 않습니다.", LogLevel.WARN),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "E-405", "지원하지 않는 HTTP 메서드입니다.", LogLevel.WARN),

    // 인증 && 인가 에러
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "A-001", "토큰이 만료되었습니다.", LogLevel.WARN),
    NOT_VALID_TOKEN(HttpStatus.UNAUTHORIZED, "A-002", "해당 토큰은 유효한 토큰이 아닙니다.", LogLevel.WARN),
    NOT_EXISTS_AUTHORIZATION(HttpStatus.UNAUTHORIZED, "A-003", "Authorization Header가 빈값입니다.", LogLevel.WARN),
    NOT_VALID_BEARER_GRANT_TYPE(HttpStatus.UNAUTHORIZED, "A-004", "인증 타입이 Bearer 타입이 아닙니다.", LogLevel.WARN),
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "A-005", "해당 refresh token은 존재하지 않습니다.", LogLevel.WARN),
    REFRESH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "A-006", "해당 refresh token은 만료됐습니다.", LogLevel.WARN),
    NOT_ACCESS_TOKEN_TYPE(HttpStatus.UNAUTHORIZED, "A-007", "해당 토큰은 ACCESS TOKEN이 아닙니다.", LogLevel.WARN),
    FORBIDDEN_ADMIN(HttpStatus.FORBIDDEN, "A-008", "관리자 Role이 아닙니다.", LogLevel.WARN),

    // 회원 에러
    INVALID_MEMBER_TYPE(HttpStatus.BAD_REQUEST, "M-001", "잘못된 회원 타입 입니다.(memberType : KAKAO)", LogLevel.WARN),
    ALREADY_REGISTERED_MEMBER(HttpStatus.BAD_REQUEST, "M-002", "이미 가입된 회원 입니다.", LogLevel.WARN),
    MEMBER_NOT_EXISTS(HttpStatus.BAD_REQUEST, "M-003", "해당 회원은 존재하지 않습니다.", LogLevel.WARN);

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;
    private final LogLevel logLevel;

    ErrorCode(HttpStatus httpStatus, String errorCode, String message, LogLevel logLevel) {
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
        this.message = message;
        this.logLevel = logLevel;
    }
}
