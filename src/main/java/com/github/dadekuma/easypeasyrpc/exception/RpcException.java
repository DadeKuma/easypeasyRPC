package com.github.dadekuma.easypeasyrpc.exception;

import com.github.dadekuma.easypeasyrpc.resource.error.Error;
import com.github.dadekuma.easypeasyrpc.resource.error.ErrorType;

public class RpcException extends Exception{
    private ErrorType errorType;

    public RpcException(ErrorType errorType) {
        this(errorType, null);
    }


    public RpcException(ErrorType errorType, Throwable cause) {
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
