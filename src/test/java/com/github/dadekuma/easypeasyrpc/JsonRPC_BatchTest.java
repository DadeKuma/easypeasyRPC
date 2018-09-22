package com.github.dadekuma.easypeasyrpc;

import com.google.gson.JsonElement;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class JsonRPC_BatchTest {
    private static RpcManager jsonRPCManager;
    @BeforeClass
    public static void setUpBeforeClass(){
        DummyMethodPerformer methodPerformerTest = new DummyMethodPerformer();
        jsonRPCManager = new RpcManager(methodPerformerTest);
        jsonRPCManager.setMethodList(methodPerformerTest.getMethodList());
    }

    @Test
    public void parseInvalidBatchArray(){
        String testRequest = "[1,2,3]";
        JsonElement jsonResponse = jsonRPCManager.parseRequest(testRequest);
        String stringResponse = jsonResponse.toString();
        String expectedResponse = "["+
                "{\"jsonrpc\":\"2.0\",\"error\":{\"code\":-32600,\"message\":\"Invalid RpcRequest\"},\"id\":null}," +
                "{\"jsonrpc\":\"2.0\",\"error\":{\"code\":-32600,\"message\":\"Invalid RpcRequest\"},\"id\":null}," +
                "{\"jsonrpc\":\"2.0\",\"error\":{\"code\":-32600,\"message\":\"Invalid RpcRequest\"},\"id\":null}" +
                "]";
        Assert.assertEquals(expectedResponse, stringResponse);
    }

    @Test
    public void parseRPCCallBatch(){
        String testRequest = "[" +
                "{\"jsonrpc\": \"2.0\", \"method\": \"return1\", \"id\": \"1\"}," +
                "{\"jsonrpc\": \"2.0\", \"method\": \"missing_notify\", \"params\": {\"parameters\":{\"value\":\"33\"}}}," +
                "{\"jsonrpc\":\"2.0\",\"method\":\"returnBiggest\",\"params\":{\"parameters\":{\"value\":[0,999]}},\"id\":\"2\"}," +
                "{\"jsonrpc\": \"2.0\", \"method\": \"returnMyParam\", \"params\": {\"parameters\":{\"value\":\"hey\"}}, \"id\": \"3\"}," +
                "{\"invalid\": \"nope\", \"id\":\"4\"}," +
                "{\"jsonrpc\": \"2.0\", \"invalid\": \"!!!\", \"id\": \"104\"}," +
                "{\"jsonrpc\": \"2.0\", \"method\": \"return1\", \"id\": \"5\"}," +
                "{\"jsonrpc\": \"2.0\", \"method\": \"return1\", \"params\": {\"parameters\":{\"invalid\":\"params\"}},\"id\": \"6\"}," +
                "{\"jsonrpc\": \"2.0\", \"method\": \"return1\", \"params\": {\"parameters\":{\"value\":[1,2,3]}},\"id\": \"7\"}," +
                "{\"jsonrpc\": \"2.0\", \"method\": \"notFound\", \"id\": \"8\"}" +
                "]";
        JsonElement jsonResponse = jsonRPCManager.parseRequest(testRequest);
        String stringResponse = jsonResponse.toString();
        String expectedResponse = "["+
                "{\"jsonrpc\":\"2.0\",\"result\":{\"value\":1},\"id\":\"1\"}," +
                "{\"jsonrpc\":\"2.0\",\"result\":{\"value\":999},\"id\":\"2\"}," +
                "{\"jsonrpc\":\"2.0\",\"result\":{\"value\":\"hey\"},\"id\":\"3\"}," +
                "{\"jsonrpc\":\"2.0\",\"error\":{\"code\":-32600,\"message\":\"Invalid RpcRequest\"},\"id\":null}," +
                "{\"jsonrpc\":\"2.0\",\"error\":{\"code\":-32600,\"message\":\"Invalid RpcRequest\"},\"id\":null}," +
                "{\"jsonrpc\":\"2.0\",\"result\":{\"value\":1},\"id\":\"5\"}," +
                "{\"jsonrpc\":\"2.0\",\"error\":{\"code\":-32602,\"message\":\"Invalid params\"},\"id\":\"6\"}," +
                "{\"jsonrpc\":\"2.0\",\"error\":{\"code\":-32602,\"message\":\"Invalid params\"},\"id\":\"7\"}," +
                "{\"jsonrpc\":\"2.0\",\"error\":{\"code\":-32601,\"message\":\"Method not found\"},\"id\":\"8\"}" +
                "]";
        Assert.assertEquals(expectedResponse, stringResponse);
    }
}
