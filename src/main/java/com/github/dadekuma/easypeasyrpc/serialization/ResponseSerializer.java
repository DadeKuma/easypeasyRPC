package com.github.dadekuma.easypeasyrpc.serialization;

import com.github.dadekuma.easypeasyrpc.RpcManager;
import com.github.dadekuma.easypeasyrpc.resource.RpcResponse;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class ResponseSerializer implements JsonSerializer<RpcResponse>{
    @Override
    public JsonElement serialize(RpcResponse response, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("jsonrpc", RpcManager.RPC_VERSION);

        if (response.getError() != null) {
            jsonObject.add("error", jsonSerializationContext.serialize(response.getError()));
        } else {
            jsonObject.add("result", jsonSerializationContext.serialize(response.getResult()));
        }
        jsonObject.addProperty("id", response.getId());
        return jsonObject;
    }
}
