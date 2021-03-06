package com.github.dadekuma.easypeasyrpc.resource.params;

import com.google.gson.*;

import java.lang.reflect.Type;

public class RpcElement {
    private JsonElement value;

    public RpcElement(Object o){
        value = new Gson().toJsonTree(o);
    }
    public RpcElement(Object... o){
        value = new Gson().toJsonTree(o);
    }

    public JsonArray getAsJsonArray(){ return value.getAsJsonArray(); }
    public JsonObject getAsJsonObject(){
        return value.getAsJsonObject();
    }
    public JsonPrimitive getAsJsonPrimitive(){
        return value.getAsJsonPrimitive();
    }
    public int getAsInt(){
        return value.getAsInt();
    }
    public String getAsString(){
        return value.getAsString();
    }
    public boolean getAsBoolean(){ return  value.getAsBoolean(); }
    public float getAsFloat() { return value.getAsFloat(); }
    public JsonElement getValue() { return value; }
    public <T> T getAsClass(Type className) { return new Gson().fromJson(value, className); }
    public boolean isJsonArray() { return value.isJsonArray(); }
    public boolean isJsonObject() { return  value.isJsonObject(); }
    public boolean isJsonPrimitive() {return  value.isJsonPrimitive(); }

    public int size() {
        if(value.isJsonArray())
            return value.getAsJsonArray().size();
        return 1;
    }
}
