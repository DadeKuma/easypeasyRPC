package com.github.dadekuma.easypeasyrpc;

import com.github.dadekuma.easypeasyrpc.exception.ErrorException;
import com.github.dadekuma.easypeasyrpc.resource.params.Element;
import com.github.dadekuma.easypeasyrpc.resource.params.ParameterList;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeoutException;
public class ClientTest {

    private static DummyCommunicator dummyCommunicator;
    private static Client client;

    @Before
    public void setUpBefore(){
        dummyCommunicator = new DummyCommunicator();
        client = new Client(dummyCommunicator);
    }

    @Test
    public void fulfillRequestPrimitiveParam() throws ErrorException, TimeoutException {
        int parameter = 1;
        Element params = new Element(parameter);
        ParameterList parameterList = new ParameterList(params);
        String methodName = "hello";

        client.fulfillRequest(methodName, parameterList);

        String expectedStringRequest = "{\"jsonrpc\":\"2.0\",\"method\":\"hello\"," +
                "\"params\":{\"parameters\":{\"value\":1}},\"id\":\"0\"}";

        Assert.assertEquals(expectedStringRequest, dummyCommunicator.sentMessage);
    }

    @Test
    public void fulfillRequestOneClassParam() throws ErrorException, TimeoutException {
        DummyClass dummyParameter = new DummyClass(10);
        Element params = new Element(dummyParameter);
        ParameterList parameterList = new ParameterList(params);
        String methodName = "hello";

        client.fulfillRequest(methodName, parameterList);

        String expectedStringRequest = "{\"jsonrpc\":\"2.0\",\"method\":\"hello\","+
        "\"params\":{\"parameters\":{\"value\":{\"example\":10.0}}},\"id\":\"0\"}";

        Assert.assertEquals(expectedStringRequest, dummyCommunicator.sentMessage);
    }

    @Test
    public void fulfillRequestMultipleParams() throws ErrorException, TimeoutException {
        Element params = new Element(1, "hello", 3.2f, true);
        ParameterList parameterList = new ParameterList(params);
        String methodName = "hello";

        client.fulfillRequest(methodName, parameterList);

        String expectedStringRequest = "{\"jsonrpc\":\"2.0\",\"method\":\"hello\"," +
                "\"params\":{\"parameters\":{\"value\":[1,\"hello\",3.2,true]}},\"id\":\"0\"}";

        Assert.assertEquals(expectedStringRequest, dummyCommunicator.sentMessage);
    }

    @Test
    public void fulfillMultipleSequentialRequest() throws ErrorException, TimeoutException{
        Element params = new Element(1, "hello", 3.2f, true);
        ParameterList parameterList = new ParameterList(params);
        String methodName = "hello";

        client.fulfillRequest(methodName, parameterList);

        String expectedStringRequest = "{\"jsonrpc\":\"2.0\",\"method\":\"hello\"," +
                "\"params\":{\"parameters\":{\"value\":[1,\"hello\",3.2,true]}},\"id\":\"0\"}";

        Assert.assertEquals(dummyCommunicator.sentMessage, expectedStringRequest);


        params = new Element(1234);
        parameterList = new ParameterList(params);
        methodName = "no";

        client.fulfillRequest(methodName, parameterList);

        expectedStringRequest = "{\"jsonrpc\":\"2.0\",\"method\":\"no\"," +
                "\"params\":{\"parameters\":{\"value\":1234}},\"id\":\"1\"}";

        Assert.assertEquals(expectedStringRequest, dummyCommunicator.sentMessage);
    }

}