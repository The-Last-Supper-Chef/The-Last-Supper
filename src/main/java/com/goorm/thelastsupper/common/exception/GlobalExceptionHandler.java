package com.goorm.thelastsupper.common.exception;

import com.goorm.thelastsupper.account.exception.AccountException;
import com.goorm.thelastsupper.common.dto.ErrorResponse;
import com.goorm.thelastsupper.reservation.exception.ReservationException;
import com.goorm.thelastsupper.restaurant.exception.RestaurantException;
import com.goorm.thelastsupper.waiting.exception.WaitingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    // Spring valid
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> validationExceptionsHandler(MethodArgumentNotValidException ex) {
        // 첫 번째 에러만 꺼내서 CustomException으로 감쌈
        FieldError fieldError = ex.getBindingResult().getFieldError();

        String message = fieldError != null ? fieldError.getDefaultMessage() : "검증 오류입니다.";

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(ErrorCode.INVALID_INPUT_PARAMETER.name(), message));
    }

    @ExceptionHandler(AccountException.class)
    public ResponseEntity<ErrorResponse> AccountExceptionHandler(AccountException ex) {
        ErrorResponse response = new ErrorResponse(ex.getErrorCode().name(), ex.getErrorCode().getMessage());
        return new ResponseEntity<>(response, ex.getErrorCode().getHttpStatus());
    }

    @ExceptionHandler(RestaurantException.class)
    public ResponseEntity<ErrorResponse> RestaurantExceptionHandler(RestaurantException ex) {
        ErrorResponse response = new ErrorResponse(ex.getErrorCode().name(), ex.getErrorCode().getMessage());
        return new ResponseEntity<>(response, ex.getErrorCode().getHttpStatus());
    }

    @ExceptionHandler(WaitingException.class)
    public ResponseEntity<ErrorResponse> waitingExceptionHandler(WaitingException ex) {
        ErrorResponse response = new ErrorResponse(ex.getErrorCode().name(), ex.getErrorCode().getMessage());
        return new ResponseEntity<>(response, ex.getErrorCode().getHttpStatus());
    }

    @ExceptionHandler(ReservationException.class)
    public ResponseEntity<ErrorResponse> ReservationHandler(ReservationException ex) {
        ErrorResponse response = new ErrorResponse(ex.getErrorCode().name(), ex.getErrorCode().getMessage());
        return new ResponseEntity<>(response, ex.getErrorCode().getHttpStatus());
    }
}
