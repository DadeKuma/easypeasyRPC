package com.github.dadekuma.easypeasyrpc;

import com.google.gson.JsonElement;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class JsonRPC_ParametersTest {
    private static RpcManager jsonRPCManager;
    @BeforeClass
    public static void setUpBeforeClass(){
        DummyMethodPerformer methodPerformerTest = new DummyMethodPerformer();
        jsonRPCManager = new RpcManager(methodPerformerTest);
        jsonRPCManager.setMethodList(methodPerformerTest.getMethodList());
    }
    @Test
    public void parseInvalidTypeParams(){
        String testRequest = "{\"jsonrpc\":\"2.0\",\"method\":\"returnBiggest\",\"params\":{\"parameters\":{\"value\":[\"notANumber\",false]}},\"id\":\"92\"}";
        JsonElement jsonResponse = jsonRPCManager.parseRequest(testRequest);
        String stringResponse = jsonResponse.toString();
        String expectedResponse = "{\"jsonrpc\":\"2.0\",\"error\":{\"code\":-32602,\"message\":\"Invalid params\"},\"id\":\"92\"}";
        Assert.assertEquals(expectedResponse, stringResponse);
    }

    @Test
    public void parseTooMuchParams(){
        String testRequest = "{\"jsonrpc\":\"2.0\",\"method\":\"returnBiggest\",\"params\":{\"parameters\":{\"value\":[1,2,3,4]}},\"id\":\"92\"}";
        JsonElement jsonResponse = jsonRPCManager.parseRequest(testRequest);
        String stringResponse = jsonResponse.toString();
        String expectedResponse = "{\"jsonrpc\":\"2.0\",\"error\":{\"code\":-32602,\"message\":\"Invalid params\"},\"id\":\"92\"}";
        Assert.assertEquals(expectedResponse, stringResponse);
    }

    @Test
    public void parseNotEnoughParams(){
        String testRequest = "{\"jsonrpc\":\"2.0\",\"method\":\"returnBiggest\",\"params\":{\"parameters\":{\"value\":[1]}},\"id\":\"92\"}";
        JsonElement jsonResponse = jsonRPCManager.parseRequest(testRequest);
        String stringResponse = jsonResponse.toString();
        String expectedResponse = "{\"jsonrpc\":\"2.0\",\"error\":{\"code\":-32602,\"message\":\"Invalid params\"},\"id\":\"92\"}";
        Assert.assertEquals(expectedResponse, stringResponse);
    }

    @Test
    public void parseNullParameters(){
        String testRequest = "{\"jsonrpc\":\"2.0\",\"method\":\"returnBiggest\",\"params\":{\"parameters\":{\"value\":[null, null]}},\"id\":\"92\"}";
        JsonElement jsonResponse = jsonRPCManager.parseRequest(testRequest);
        String stringResponse = jsonResponse.toString();
        String expectedResponse = "{\"jsonrpc\":\"2.0\",\"error\":{\"code\":-32602,\"message\":\"Invalid params\"},\"id\":\"92\"}";
        Assert.assertEquals(expectedResponse, stringResponse);
    }

    @Test
    public void parseNullParams(){
        String testRequest = "{\"jsonrpc\":\"2.0\",\"method\":\"returnBiggest\",\"params\":null,\"id\":\"92\"}";
        JsonElement jsonResponse = jsonRPCManager.parseRequest(testRequest);
        String stringResponse = jsonResponse.toString();
        String expectedResponse = "{\"jsonrpc\":\"2.0\",\"error\":{\"code\":-32602,\"message\":\"Invalid params\"},\"id\":\"92\"}";
        Assert.assertEquals(expectedResponse, stringResponse);
    }

    @Test
    public void parseWrongParameters(){
        String testRequest = "{\"jsonrpc\":\"2.0\",\"method\":\"operateClass\",\"params\":{\"parameters\":{\"value\":[2]}},\"id\":\"92\"}";
        JsonElement jsonResponse = jsonRPCManager.parseRequest(testRequest);
        String stringResponse = jsonResponse.toString();
        String expectedResponse = "{\"jsonrpc\":\"2.0\",\"error\":{\"code\":-32602,\"message\":\"Invalid params\"},\"id\":\"92\"}";
        Assert.assertEquals(expectedResponse, stringResponse);
    }
}
