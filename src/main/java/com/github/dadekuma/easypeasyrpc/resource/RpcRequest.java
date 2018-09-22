package com.github.dadekuma.easypeasyrpc.resource;

import com.github.dadekuma.easypeasyrpc.resource.params.RpcParameterList;

public class RpcRequest {
    private final String method;
    private final RpcParameterList params;
    private final String id;
    private final String jsonrpc;

    public RpcRequest(String method, RpcParameterList params, String id, String jsonrpc) {
        this.method = method;
        this.params = params;
        this.id = id;
        this.jsonrpc = jsonrpc;
    }

    public boolean isNotification(){
        return (id == null || id.isEmpty());
    }

    public String getMethod() {
        return method;
    }

    public RpcParameterList getParams() {
        return params;
    }

    public String getId() {
        return id;
    }

    public String getJsonRPC() {
        return jsonrpc;
    }
}
