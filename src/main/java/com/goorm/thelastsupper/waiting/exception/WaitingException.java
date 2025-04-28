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

    //Message:  대기열이 이미 중단되어 있습니다.
    public static class WaitingAlreadyPausedException extends  WaitingException {
        public WaitingAlreadyPausedException() {
            super(WaitingErrorCode.WAITING_ALREADY_PAUSED);
        }
    }

    //Message: 대기열이 열려있지 않습니다.
    public static class WaitingNotOpendException extends WaitingException {
        public WaitingNotOpendException() {
            super(WaitingErrorCode.WAITING_NOT_OPENED);
        }
    }

    //Message: 대기열이 이미 오픈되어 있습니다.
    public static class WaitingAlreadyOpenException extends WaitingException {
        public WaitingAlreadyOpenException() {
            super(WaitingErrorCode.WAITING_ALREADY_OPEN);
        }
    }

    //Message: 해당 매장을 찾을 수 없습니다.
    public static class RestaurantNotFoundException extends WaitingException {
        public RestaurantNotFoundException() {
            super(WaitingErrorCode.RESTAURANT_NOT_FOUND);
        }
    }

    //Message: 대기열이 이미 종료되었습니다.
    public static class WaitingAlreadyClosedException extends WaitingException {
        public WaitingAlreadyClosedException() {
            super(WaitingErrorCode.WAITING_ALREADY_CLOSED);
        }
    }

    //Message: 매장 ID는 필수 값입니다.
    public static class RestaurantIdRequiredException extends WaitingException {
        public RestaurantIdRequiredException() {
            super(WaitingErrorCode.RESTAURANT_ID_REQUIRED);
        }
    }
}
