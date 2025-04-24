package com.goorm.thelastsupper.account.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AccountException extends RuntimeException {

    private final AccountErrorCode errorCode;

    public static class AccountDuplicationException extends AccountException {
        public AccountDuplicationException() {
            super(AccountErrorCode.EMAIL_DUPLICATION);
        }
    }
}
