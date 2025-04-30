package com.goorm.thelastsupper.common.security.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AuthException extends RuntimeException {

	private final AuthErrorCode errorCode;

	public static class MissingAuthHeaderException extends AuthException {
		public MissingAuthHeaderException() {
			super(AuthErrorCode.AUTH_HEADER_MISSING);
		}
	}

	public static class InvalidAuthHeaderException extends AuthException {
		public InvalidAuthHeaderException() {
			super(AuthErrorCode.AUTH_HEADER_INVALID);
		}
	}

	public static class MissingBearerPrefixException extends AuthException {
		public MissingBearerPrefixException() {
			super(AuthErrorCode.TOKEN_PREFIX_MISSING);
		}
	}

	public static class TokenParsingException extends AuthException {
		public TokenParsingException() {
			super(AuthErrorCode.TOKEN_PARSING_FAILED);
		}
	}

	public static class TokenExpiredException extends AuthException {
		public TokenExpiredException() {
			super(AuthErrorCode.TOKEN_EXPIRED);
		}
	}

	public static class UnsupportedTokenException extends AuthException {
		public UnsupportedTokenException() {
			super(AuthErrorCode.TOKEN_UNSUPPORTED);
		}
	}

	public static class InvalidClaimValueException extends AuthException {
		public InvalidClaimValueException() {
			super(AuthErrorCode.INVALID_CLAIM_VALUE);
		}
	}

}
