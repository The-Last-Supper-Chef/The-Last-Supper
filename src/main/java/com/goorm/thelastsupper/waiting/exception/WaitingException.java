package com.goorm.thelastsupper.waiting.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class WaitingException extends RuntimeException{

    private final WaitingErrorCode errorCode;

    public static class WaitingNotFoundException extends WaitingException {
        public WaitingNotFoundException() {
            super(WaitingErrorCode.WAITING_NOT_FOUND);
        }
    }

    public static class AlreadyWaitingException extends WaitingException {
        public AlreadyWaitingException() {
            super(WaitingErrorCode.ALREADY_WAITING);
        }
    }

    public static class AccountNotFoundException extends WaitingException {
        public AccountNotFoundException() {
            super(WaitingErrorCode.ACCOUNT_NOT_FOUND);
        }
    }

    public static class WaitingNotOpenException extends WaitingException {
        public WaitingNotOpenException() {
            super(WaitingErrorCode.WAITING_NOT_OPEN);
        }
    }

    public static class AlreadyLastWaitingException extends WaitingException {
        public AlreadyLastWaitingException() {
            super(WaitingErrorCode.ALREADY_LAST_WAITING);
        }
    }

    public static class WaitingSettingNotFoundException extends WaitingException {
        public WaitingSettingNotFoundException() {
            super(WaitingErrorCode.WAITING_SETTING_NOT_FOUND);
        }
    }
}
