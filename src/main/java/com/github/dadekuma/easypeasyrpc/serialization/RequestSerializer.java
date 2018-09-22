package com.github.dadekuma.easypeasyrpc.serialization;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class RequestSerializer implements JsonSerializer<com.github.dadekuma.easypeasyrpc.resource.RpcRequest> {
    @Override
    public JsonElement serialize(com.github.dadekuma.easypeasyrpc.resource.RpcRequest request, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("jsonrpc", com.github.dadekuma.easypeasyrpc.RpcManager.RPC_VERSION);
        jsonObject.addProperty("method", request.getMethod());
        if (request.getParams() != null) {
            jsonObject.add("params", jsonSerializationContext.serialize(request.getParams()));
        }
        jsonObject.addProperty("id", request.getId());
        return jsonObject;
    }
}