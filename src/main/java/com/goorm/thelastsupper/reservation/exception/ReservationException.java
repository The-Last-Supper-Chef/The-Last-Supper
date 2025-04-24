package com.goorm.thelastsupper.reservation.exception;

import com.goorm.thelastsupper.waiting.exception.WaitingErrorCode;
import com.goorm.thelastsupper.waiting.exception.WaitingException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ReservationException extends RuntimeException{

    private final ReservationErrorCode errorCode;

    public static class SlotNotFoundException extends ReservationException{
        public SlotNotFoundException(){
            super(ReservationErrorCode.RESERVATION_SLOT_NOT_FOUND);
        }
    }
}
