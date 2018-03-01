package com.github.dadekuma.easypeasyrpc.resource.error;

import com.github.dadekuma.easypeasyrpc.resource.params.Element;

public class Error {
    private int code;
    private String message;
    private Element data;

    public Error(ErrorType errorType){
        this(errorType, null);
    }
    public Error(ErrorType errorType, Element data) {
        switch (errorType) {
            case PARSE_ERROR:
                setParams(-32700, "Parse error");
                break;
            case INVALID_REQUEST:
                setParams(-32600, "Invalid Request");
                break;
            case METHOD_NOT_FOUND:
                setParams(-32601, "Method not found");
                break;
            case INVALID_PARAMS:
                setParams(-32602, "Invalid params");
                break;
            case INTERNAL_ERROR:
                setParams(-32603, "Internal error");
                break;
        }
        this.data = data;
    }

    private void setParams(int code, String message){
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public Element getData() { return data; }

    public void setMessage(String message) {
        this.message = message;
    }
}
