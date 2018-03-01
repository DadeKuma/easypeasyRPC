package com.github.dadekuma.easypeasyrpc.resource;

import com.github.dadekuma.easypeasyrpc.resource.error.Error;
import com.github.dadekuma.easypeasyrpc.resource.error.ErrorType;
import com.github.dadekuma.easypeasyrpc.resource.params.Element;

public class Response {
    private Error error;
    private Element result;
    private String id;
    private String jsonrpc;

    public Response(ErrorType error, String id, String jsonrpc) {
        this.error = new Error(error);
        this.id = id;
        this.jsonrpc = jsonrpc;
    }

    public Response(Element result, String id, String jsonrpc) {
        this.result = result;
        this.id = id;
        this.jsonrpc = jsonrpc;
    }

    public Error getError() {
        return error;
    }

    public Element getResult() {
        return result;
    }

    public String getId() {
        return id;
    }

    public String getJsonrpc() {
        return jsonrpc;
    }
}
