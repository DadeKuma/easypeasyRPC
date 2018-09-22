package com.github.dadekuma.easypeasyrpc.exception.message;

public enum GenericExceptionMessage {
    BAD_INDEX("Trying to access negative or out of bound index"),
    INVALID_COMMUNICATOR("RpcCommunicator is not set"),
    NEGATIVE_NUMBER_OF_PARAMETERS("Number of parameters must be non negative"),
    CONNECTION_TIMEOUT("Connection timeout");

    private String message;
    GenericExceptionMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }
}
