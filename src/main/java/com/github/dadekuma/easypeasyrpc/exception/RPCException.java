package com.github.dadekuma.easypeasyrpc.exception;

import com.github.dadekuma.easypeasyrpc.resource.error.Error;
import com.github.dadekuma.easypeasyrpc.resource.error.ErrorType;

public class RPCException extends Exception{
    private ErrorType errorType;

    public RPCException(ErrorType errorType) {
        this(errorType, null);
    }


    public RPCException(ErrorType errorType, Throwable cause) {
        super(cause);
        this.errorType = errorType;
    }

    public Error getError() {
        return new Error(errorType);
    }

    public ErrorType getErrorType(){
        return errorType;
    }
}
