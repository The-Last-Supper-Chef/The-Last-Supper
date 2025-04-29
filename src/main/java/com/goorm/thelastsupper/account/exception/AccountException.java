package com.goorm.thelastsupper.account.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AccountException extends RuntimeException {

    private final AccountErrorCode errorCode;

    public static class AccountNotFoundException extends AccountException {
        public AccountNotFoundException() {
            super(AccountErrorCode.ACCOUNT_NOT_FOUND);
        }
    }

    public static class AccountDuplicationException extends AccountException {
        public AccountDuplicationException() {
            super(AccountErrorCode.EMAIL_DUPLICATION);
        }
    }

    public static class AccountWithdrawnException extends AccountException {
        public AccountWithdrawnException() {
            super(AccountErrorCode.ACCOUNT_WITHDRAWN);
        }
    }
}
