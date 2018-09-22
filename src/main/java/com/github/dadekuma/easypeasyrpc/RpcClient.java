package com.github.dadekuma.easypeasyrpc;

import com.github.dadekuma.easypeasyrpc.exception.CommunicatorException;
import com.github.dadekuma.easypeasyrpc.exception.ErrorException;
import com.github.dadekuma.easypeasyrpc.exception.message.GenericExceptionMessage;
import com.github.dadekuma.easypeasyrpc.resource.RpcCommunicator;
import com.github.dadekuma.easypeasyrpc.resource.RpcResponse;
import com.github.dadekuma.easypeasyrpc.resource.params.ParameterList;
import com.github.dadekuma.easypeasyrpc.serialization.RequestSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeoutException;

public abstract class RpcClient {

    private final Gson gson;
    private RpcCommunicator communicator;
    private Integer requestNumber;

    public RpcClient() {
        this(null);
    }

    public RpcClient(RpcCommunicator communicator) {
        this.communicator = communicator;
        requestNumber = 0;
        gson = new GsonBuilder()
                .registerTypeAdapter(com.github.dadekuma.easypeasyrpc.resource.RpcRequest.class, new RequestSerializer())
                .create();
    }

    public RpcResponse fulfillRequest(String methodName, ParameterList params) throws ErrorException, TimeoutException {
        try {
            com.github.dadekuma.easypeasyrpc.resource.RpcRequest request = new com.github.dadekuma.easypeasyrpc.resource.RpcRequest(methodName, params, requestNumber.toString(), com.github.dadekuma.easypeasyrpc.RpcManager.RPC_VERSION);
            sendRequest(request);
            if (!request.isNotification()) {
                return receiveResponse();
            }
        } catch (CommunicatorException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void sendRequest(com.github.dadekuma.easypeasyrpc.resource.RpcRequest request) throws CommunicatorException {
        try {
            ++requestNumber;
            String stringRequest = gson.toJson(request);
            communicator.sendMsg(stringRequest);
        } catch (NullPointerException e) {
            throw new CommunicatorException(GenericExceptionMessage.INVALID_COMMUNICATOR.toString());
        }
    }


    private RpcResponse receiveResponse() throws CommunicatorException, ErrorException, TimeoutException {
        try {
            String stringResponse = communicator.receiveMsg();
            RpcResponse response = gson.fromJson(stringResponse, RpcResponse.class);
            if (response.getError() != null)
                throw new ErrorException(response.getError());
            return response;
        } catch (TimeoutException e) {
            communicator.dispose();
            throw e;
        } catch (NullPointerException e) {
            throw new CommunicatorException(GenericExceptionMessage.INVALID_COMMUNICATOR.toString());
        }
    }

    public void setCommunicator(RpcCommunicator communicator) {
        this.communicator = communicator;
    }

    public RpcCommunicator getCommunicator() {
        return communicator;
    }

    public Gson getGson() {
        return gson;
    }
}
