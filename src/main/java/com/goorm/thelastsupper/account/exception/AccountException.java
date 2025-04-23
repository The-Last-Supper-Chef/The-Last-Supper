package com.goorm.thelastsupper.account.exception;

import com.goorm.thelastsupper.common.exception.ErrorCode;

public class AccountException extends RuntimeException{
    private ErrorCode errorCode;
    private Object data;

    private AccountException(ErrorCode errorCode){
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.data = null;
    }

    private AccountException(ErrorCode errorCode, Object data){
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.data = data;
    }
}
