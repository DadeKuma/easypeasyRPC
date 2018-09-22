package com.github.dadekuma.easypeasyrpc;

import com.github.dadekuma.easypeasyrpc.exception.ErrorException;
import com.github.dadekuma.easypeasyrpc.resource.params.RpcElement;
import com.github.dadekuma.easypeasyrpc.resource.params.RpcParameterList;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeoutException;
public class ClientTest {

    private static DummyCommunicator dummyCommunicator;
    private static DummyClient client;

    @Before
    public void setUpBefore(){
        dummyCommunicator = new DummyCommunicator();
        client = new DummyClient(dummyCommunicator);
    }

    @Test
    public void fulfillRequestPrimitiveParam() throws ErrorException, TimeoutException {
        int parameter = 1;
        RpcElement params = new RpcElement(parameter);
        RpcParameterList parameterList = new RpcParameterList(params);
        String methodName = "hello";

        client.fulfillRequest(methodName, parameterList);

        String expectedStringRequest = "{\"jsonrpc\":\"2.0\",\"method\":\"hello\"," +
                "\"params\":{\"parameters\":{\"value\":1}},\"id\":\"0\"}";

        Assert.assertEquals(expectedStringRequest, dummyCommunicator.sentMessage);
    }

    @Test
    public void fulfillRequestOneClassParam() throws ErrorException, TimeoutException {
        DummyClass dummyParameter = new DummyClass(10);
        RpcElement params = new RpcElement(dummyParameter);
        RpcParameterList parameterList = new RpcParameterList(params);
        String methodName = "hello";

        client.fulfillRequest(methodName, parameterList);

        String expectedStringRequest = "{\"jsonrpc\":\"2.0\",\"method\":\"hello\","+
        "\"params\":{\"parameters\":{\"value\":{\"example\":10.0}}},\"id\":\"0\"}";

        Assert.assertEquals(expectedStringRequest, dummyCommunicator.sentMessage);
    }

    @Test
    public void fulfillRequestMultipleParams() throws ErrorException, TimeoutException {
        RpcElement params = new RpcElement(1, "hello", 3.2f, true);
        RpcParameterList parameterList = new RpcParameterList(params);
        String methodName = "hello";

        client.fulfillRequest(methodName, parameterList);

        String expectedStringRequest = "{\"jsonrpc\":\"2.0\",\"method\":\"hello\"," +
                "\"params\":{\"parameters\":{\"value\":[1,\"hello\",3.2,true]}},\"id\":\"0\"}";

        Assert.assertEquals(expectedStringRequest, dummyCommunicator.sentMessage);
    }

    @Test
    public void fulfillMultipleSequentialRequest() throws ErrorException, TimeoutException{
        RpcElement params = new RpcElement(1, "hello", 3.2f, true);
        RpcParameterList parameterList = new RpcParameterList(params);
        String methodName = "hello";

        client.fulfillRequest(methodName, parameterList);

        String expectedStringRequest = "{\"jsonrpc\":\"2.0\",\"method\":\"hello\"," +
                "\"params\":{\"parameters\":{\"value\":[1,\"hello\",3.2,true]}},\"id\":\"0\"}";

        Assert.assertEquals(dummyCommunicator.sentMessage, expectedStringRequest);


        params = new RpcElement(1234);
        parameterList = new RpcParameterList(params);
        methodName = "no";

        client.fulfillRequest(methodName, parameterList);

        expectedStringRequest = "{\"jsonrpc\":\"2.0\",\"method\":\"no\"," +
                "\"params\":{\"parameters\":{\"value\":1234}},\"id\":\"1\"}";

        Assert.assertEquals(expectedStringRequest, dummyCommunicator.sentMessage);
    }

}