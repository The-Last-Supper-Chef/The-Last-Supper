package com.goorm.thelastsupper.common.security.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode {
	AUTH_HEADER_MISSING(HttpStatus.BAD_REQUEST, "Authorization 헤더가 존재하지 않습니다."),
	AUTH_HEADER_INVALID(HttpStatus.BAD_REQUEST, "잘못된 형식의 Authorization 헤더입니다."),
	TOKEN_PREFIX_MISSING(HttpStatus.BAD_REQUEST, "Bearer 접두사가 없습니다."),
	TOKEN_PARSING_FAILED(HttpStatus.UNAUTHORIZED, "토큰 파싱에 실패했습니다."),
	TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다."),
	TOKEN_UNSUPPORTED(HttpStatus.UNAUTHORIZED, "지원하지 않는 토큰 형식입니다."),
	INVALID_CLAIM_VALUE(HttpStatus.BAD_REQUEST, "토큰 내 권한 정보가 올바르지 않습니다."),

	REFRESH_TOKEN_MISSING(HttpStatus.BAD_REQUEST,     "리프레시 토큰이 없습니다."),
	REFRESH_NOT_FOUND(HttpStatus.NOT_FOUND,     "리프레시 토큰이 없습니다."),
	REFRESH_TOKEN_INVALID(HttpStatus.UNAUTHORIZED,    "유효하지 않은 리프레시 토큰입니다.");

	private final HttpStatus httpStatus;
	private final String message;
}

