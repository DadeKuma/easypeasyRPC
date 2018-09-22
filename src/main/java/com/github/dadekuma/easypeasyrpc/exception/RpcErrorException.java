package com.github.dadekuma.easypeasyrpc.exception;

import com.github.dadekuma.easypeasyrpc.resource.error.RpcError;

public class RpcErrorException extends Exception {
    private RpcError error;

    public RpcErrorException(RpcError error) {
        this.error = error;
    }

    public RpcError getError() {
        return error;
    }
}
