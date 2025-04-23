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
}
