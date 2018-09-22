package com.github.dadekuma.easypeasyrpc.exception;

import com.github.dadekuma.easypeasyrpc.resource.error.RpcError;

public class ErrorException extends Exception {
    private RpcError error;

    public ErrorException(RpcError error) {
        this.error = error;
    }

    public RpcError getError() {
        return error;
    }
}
