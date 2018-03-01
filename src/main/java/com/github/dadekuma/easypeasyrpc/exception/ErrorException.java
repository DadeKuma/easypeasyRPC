package com.github.dadekuma.easypeasyrpc.exception;

import com.github.dadekuma.easypeasyrpc.resource.error.Error;

public class ErrorException extends Exception {
    private Error error;

    public ErrorException(Error error) {
        this.error = error;
    }

    public Error getError() {
        return error;
    }
}
