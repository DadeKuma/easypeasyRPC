package com.github.dadekuma.easypeasyrpc;

import com.google.gson.JsonElement;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class JsonRPC_BasicTest {

    private static JsonRPCManager jsonRPCManager;
    @BeforeClass
    public static void setUpBeforeClass(){
        DummyMethodPerformer methodPerformerTest = new DummyMethodPerformer();
        jsonRPCManager = new JsonRPCManager(methodPerformerTest);
        jsonRPCManager.setMethodList(methodPerformerTest.getMethodList());
    }

    @Test
    public void parseGoodRequest(){
        String testRequest = "{\"jsonrpc\":\"2.0\",\"method\":\"returnBiggest\",\"params\":{\"parameters\":{\"value\":[200,100]}},\"id\":\"0\"}";
        JsonElement jsonResponse = jsonRPCManager.parseRequest(testRequest);
        String stringResponse = jsonResponse.toString();
        String expectedResponse = "{\"jsonrpc\":\"2.0\",\"result\":{\"value\":200},\"id\":\"0\"}";
        Assert.assertEquals(expectedResponse, stringResponse);
    }

    @Test
    public void parseGoodShuffledRequest(){
        String testRequest = "{\"method\":\"returnBiggest\",\"id\":\"0\",\"jsonrpc\":\"2.0\",\"params\":{\"parameters\":{\"value\":[200,100]}}}";
        JsonElement jsonResponse = jsonRPCManager.parseRequest(testRequest);
        String stringResponse = jsonResponse.toString();
        String expectedResponse = "{\"jsonrpc\":\"2.0\",\"result\":{\"value\":200},\"id\":\"0\"}";
        Assert.assertEquals(expectedResponse, stringResponse);
    }

    @Test
    public void parseNotification(){
        String testRequest = "{\"jsonrpc\": \"2.0\", \"method\": \"notification\"}";
        JsonElement jsonResponse = jsonRPCManager.parseRequest(testRequest);
        Assert.assertEquals(null, jsonResponse);
    }

    @Test
    public void parseEmptyArray() {
        String testRequest = "[]";
        JsonElement jsonResponse = jsonRPCManager.parseRequest(testRequest);
        String stringResponse = jsonResponse.toString();
        String expectedResponse = "{\"jsonrpc\":\"2.0\",\"error\":{\"code\":-32600,\"message\":\"Invalid Request\"},\"id\":null}";
        Assert.assertEquals(expectedResponse, stringResponse);
    }

    @Test
    public void parseInvalidSingleArray() {
        String testRequest = "[1]";
        JsonElement jsonResponse = jsonRPCManager.parseRequest(testRequest);
        String stringResponse = jsonResponse.toString();
        String expectedResponse = "["+
                "{\"jsonrpc\":\"2.0\",\"error\":{\"code\":-32600,\"message\":\"Invalid Request\"},\"id\":null}" +
                "]";
        Assert.assertEquals(expectedResponse, stringResponse);
    }
}
