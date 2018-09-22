package com.github.dadekuma.easypeasyrpc.serialization;

import com.github.dadekuma.easypeasyrpc.resource.error.RpcError;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class ErrorSerializer implements JsonSerializer<RpcError> {
    @Override
    public JsonElement serialize(RpcError error, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("code", error.getCode());
        jsonObject.addProperty("message", error.getMessage());

        if (error.getData() != null) {
            jsonObject.add("data", jsonSerializationContext.serialize(error.getData()));
        }
        return jsonObject;
    }
}
