package com.github.dadekuma.easypeasyrpc.exception.message;

public enum RPCExceptionMessage {
    BAD_METHOD_NAME("Method names that begin with \"rpc.\" are reserved for rpc-internal methods and extensions");


    private String message;
    RPCExceptionMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }
}
