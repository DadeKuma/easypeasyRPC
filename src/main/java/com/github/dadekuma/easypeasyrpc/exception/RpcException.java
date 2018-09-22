package com.github.dadekuma.easypeasyrpc.exception;

import com.github.dadekuma.easypeasyrpc.resource.error.RpcError;
import com.github.dadekuma.easypeasyrpc.resource.error.RpcErrorType;

public class RpcException extends Exception{
    private RpcErrorType errorType;

    public RpcException(RpcErrorType errorType) {
        this(errorType, null);
    }


    public RpcException(RpcErrorType errorType, Throwable cause) {
        super(cause);
        this.errorType = errorType;
    }

    public RpcError getError() {
        return new RpcError(errorType);
    }

    public RpcErrorType getErrorType(){
        return errorType;
    }
}
