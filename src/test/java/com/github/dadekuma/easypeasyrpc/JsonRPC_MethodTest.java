package com.github.dadekuma.easypeasyrpc;

import com.google.gson.JsonElement;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class JsonRPC_MethodTest {
    private static JsonRPCManager jsonRPCManager;
    @BeforeClass
    public static void setUpBeforeClass(){
        DummyMethodPerformer methodPerformerTest = new DummyMethodPerformer();
        jsonRPCManager = new JsonRPCManager(methodPerformerTest);
        jsonRPCManager.setMethodList(methodPerformerTest.getMethodList());
    }

    @Test
    public void parseNonExistentMethod() {
        String testRequest = "{\"jsonrpc\": \"2.0\", \"method\": \"foobar\", \"id\": \"1\"}";
        JsonElement jsonResponse = jsonRPCManager.parseRequest(testRequest);
        String stringResponse = jsonResponse.toString();
        String expectedResponse = "{\"jsonrpc\":\"2.0\",\"error\":{\"code\":-32601,\"message\":\"Method not found\"},\"id\":\"1\"}";
        Assert.assertEquals(expectedResponse, stringResponse);
    }

    @Test
    public void parseNullMethod() {
        String testRequest = "{\"jsonrpc\": \"2.0\", \"method\": null, \"id\": \"1\"}";
        JsonElement jsonResponse = jsonRPCManager.parseRequest(testRequest);
        String stringResponse = jsonResponse.toString();
        String expectedResponse = "{\"jsonrpc\":\"2.0\",\"error\":{\"code\":-32600,\"message\":\"Invalid Request\"},\"id\":null}";
        Assert.assertEquals(expectedResponse, stringResponse);
    }
}
