package com.github.dadekuma.easypeasyrpc.serialization;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.github.dadekuma.easypeasyrpc.JsonRPCManager;
import com.github.dadekuma.easypeasyrpc.resource.Response;

import java.lang.reflect.Type;

public class ResponseSerializer implements JsonSerializer<Response>{
    @Override
    public JsonElement serialize(Response response, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("jsonrpc", JsonRPCManager.RPC_VERSION);

        if (response.getError() != null) {
            jsonObject.add("error", jsonSerializationContext.serialize(response.getError()));
        } else {
            jsonObject.add("result", jsonSerializationContext.serialize(response.getResult()));
        }
        jsonObject.addProperty("id", response.getId());
        return jsonObject;
    }
}
