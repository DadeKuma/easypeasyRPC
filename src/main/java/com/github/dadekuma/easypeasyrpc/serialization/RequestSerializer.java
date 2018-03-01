package com.github.dadekuma.easypeasyrpc.serialization;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.github.dadekuma.easypeasyrpc.JsonRPCManager;
import com.github.dadekuma.easypeasyrpc.resource.Request;

import java.lang.reflect.Type;

public class RequestSerializer implements JsonSerializer<Request> {
    @Override
    public JsonElement serialize(Request request, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("jsonrpc", JsonRPCManager.RPC_VERSION);
        jsonObject.addProperty("method", request.getMethod());
        if (request.getParams() != null) {
            jsonObject.add("params", jsonSerializationContext.serialize(request.getParams()));
        }
        jsonObject.addProperty("id", request.getId());
        return jsonObject;
    }
}