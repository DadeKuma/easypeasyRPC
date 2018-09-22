package com.github.dadekuma.easypeasyrpc.resource;

import com.github.dadekuma.easypeasyrpc.resource.error.RpcError;
import com.github.dadekuma.easypeasyrpc.resource.error.RpcErrorType;
import com.github.dadekuma.easypeasyrpc.resource.params.RpcElement;

public class RpcResponse {
    private RpcError error;
    private RpcElement result;
    private String id;
    private String jsonrpc;

    public RpcResponse(RpcErrorType error, String id, String jsonrpc) {
        this.error = new RpcError(error);
        this.id = id;
        this.jsonrpc = jsonrpc;
    }

    public RpcResponse(RpcElement result, String id, String jsonrpc) {
        this.result = result;
        this.id = id;
        this.jsonrpc = jsonrpc;
    }

    public RpcError getError() {
        return error;
    }

    public RpcElement getResult() {
        return result;
    }

    public String getId() {
        return id;
    }

    public String getJsonrpc() {
        return jsonrpc;
    }
}
