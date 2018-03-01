package com.github.dadekuma.easypeasyrpc.serialization;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.github.dadekuma.easypeasyrpc.resource.error.Error;

import java.lang.reflect.Type;

public class ErrorSerializer implements JsonSerializer<Error> {
    @Override
    public JsonElement serialize(Error error, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("code", error.getCode());
        jsonObject.addProperty("message", error.getMessage());

        if (error.getData() != null) {
            jsonObject.add("data", jsonSerializationContext.serialize(error.getData()));
        }
        return jsonObject;
    }
}
