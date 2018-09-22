package com.github.dadekuma.easypeasyrpc;

import com.google.gson.JsonElement;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class JsonRPC_MalformedRequestTest {
    private static RpcManager jsonRPCManager;
    @BeforeClass
    public static void setUpBeforeClass(){
        DummyMethodPerformer methodPerformerTest = new DummyMethodPerformer();
        jsonRPCManager = new RpcManager(methodPerformerTest);
        jsonRPCManager.setMethodList(methodPerformerTest.getMethodList());
    }
    @Test
    public void parseNotValidJsonrpc(){
        String testRequest = "{\"jsonrpc\":\"100\",\"method\":\"returnBiggest\",\"params\":{\"parameters\":{\"value\":[200,100]}},\"id\":\"0\"}";
        JsonElement jsonResponse = jsonRPCManager.parseRequest(testRequest);
        String stringResponse = jsonResponse.toString();
        String expectedResponse = "{\"jsonrpc\":\"2.0\",\"error\":{\"code\":-32600,\"message\":\"Invalid RpcRequest\"},\"id\":null}";
        Assert.assertEquals(expectedResponse, stringResponse);
    }

    @Test
    public void parseNullJsonRPC() {
        String testRequest = "{\"jsonrpc\": null, \"method\": \"something\", \"id\": \"1\"}";
        JsonElement jsonResponse = jsonRPCManager.parseRequest(testRequest);
        String stringResponse = jsonResponse.toString();
        String expectedResponse = "{\"jsonrpc\":\"2.0\",\"error\":{\"code\":-32600,\"message\":\"Invalid RpcRequest\"},\"id\":null}";
        Assert.assertEquals(expectedResponse, stringResponse);
    }

    @Test
    public void parseMalformedJson1() {
        String testRequest = "{\"jsonrpc\": \"2.0\", \"method\"";
        JsonElement jsonResponse = jsonRPCManager.parseRequest(testRequest);
        String stringResponse = jsonResponse.toString();
        String expectedResponse = "{\"jsonrpc\":\"2.0\",\"error\":{\"code\":-32700,\"message\":\"Parse error\"},\"id\":null}";
        Assert.assertEquals(expectedResponse, stringResponse);
    }

    @Test
    public void parseMalformedJson2() {
        String testRequest = "{xxxxx[][][]]]]]][[[[}";
        JsonElement jsonResponse = jsonRPCManager.parseRequest(testRequest);
        String stringResponse = jsonResponse.toString();
        String expectedResponse = "{\"jsonrpc\":\"2.0\",\"error\":{\"code\":-32700,\"message\":\"Parse error\"},\"id\":null}";
        Assert.assertEquals(expectedResponse, stringResponse);
    }

    @Test
    public void parseMalformedJson3() {
        String testRequest = "1234{\"jsonrpc\": \"2.0\", \"method\": \"return1\", \"id\": \"1\"}";
        JsonElement jsonResponse = jsonRPCManager.parseRequest(testRequest);
        String stringResponse = jsonResponse.toString();
        String expectedResponse = "{\"jsonrpc\":\"2.0\",\"error\":{\"code\":-32700,\"message\":\"Parse error\"},\"id\":null}";
        Assert.assertEquals(expectedResponse, stringResponse);
    }

    @Test
    public void parseMalformedJson4() {
        String testRequest = "][{";
        JsonElement jsonResponse = jsonRPCManager.parseRequest(testRequest);
        String stringResponse = jsonResponse.toString();
        String expectedResponse = "{\"jsonrpc\":\"2.0\",\"error\":{\"code\":-32700,\"message\":\"Parse error\"},\"id\":null}";
        Assert.assertEquals(expectedResponse, stringResponse);
    }

    @Test
    public void parseEmptyRequest() {
        String testRequest = "";
        JsonElement jsonResponse = jsonRPCManager.parseRequest(testRequest);
        String stringResponse = jsonResponse.toString();
        String expectedResponse = "{\"jsonrpc\":\"2.0\",\"error\":{\"code\":-32600,\"message\":\"Invalid RpcRequest\"},\"id\":null}";
        Assert.assertEquals(expectedResponse, stringResponse);
    }

    @Test
    public void parseNullRequest() {
        String testRequest = null;
        JsonElement jsonResponse = jsonRPCManager.parseRequest(testRequest);
        String stringResponse = jsonResponse.toString();
        String expectedResponse = "{\"jsonrpc\":\"2.0\",\"error\":{\"code\":-32600,\"message\":\"Invalid RpcRequest\"},\"id\":null}";
        Assert.assertEquals(expectedResponse, stringResponse);
    }

    @Test
    public void parseInvalidRequest() {
        String testRequest = "{\"jsonrpc\":\"2.0\",\"nope\":nope,\"id\":\"2\"}";
        JsonElement jsonResponse = jsonRPCManager.parseRequest(testRequest);
        String stringResponse = jsonResponse.toString();
        String expectedResponse = "{\"jsonrpc\":\"2.0\",\"error\":{\"code\":-32600,\"message\":\"Invalid RpcRequest\"},\"id\":null}";
        Assert.assertEquals(expectedResponse, stringResponse);
    }
}
