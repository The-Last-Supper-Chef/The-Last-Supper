package com.goorm.thelastsupper.common.util;

import com.goorm.thelastsupper.common.security.exception.AuthException;

import jakarta.servlet.http.HttpServletRequest;

public class HeaderUtil {
	public static final String HEADER_AUTHORIZATION = "Authorization";
	public static final String TOKEN_PREFIX = "Bearer ";

	private HeaderUtil(){
		throw new IllegalStateException("Header Utils");
	}

	public static String getAccessToken(HttpServletRequest request) {
		String header = request.getHeader(HEADER_AUTHORIZATION);
		if (header == null) {
			throw new AuthException.MissingAuthHeaderException();
		}
		return extractToken(header);
	}

	public static String getAccessToken(String headerValue) {
		if (headerValue == null) {
			throw new AuthException.InvalidAuthHeaderException();
		}
		return extractToken(headerValue);
	}

	private static String extractToken(String headerValue) {
		if (!headerValue.startsWith(TOKEN_PREFIX)) {
			throw new AuthException.MissingBearerPrefixException();
		}
		String token = headerValue.substring((TOKEN_PREFIX).length());
		if (token.isBlank()) {
			throw new AuthException.InvalidAuthHeaderException();
		}
		return token;
	}
}
