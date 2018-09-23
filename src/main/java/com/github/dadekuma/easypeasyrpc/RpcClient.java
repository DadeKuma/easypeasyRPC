package com.github.dadekuma.easypeasyrpc;

import com.github.dadekuma.easypeasyrpc.exception.RpcCommunicatorException;
import com.github.dadekuma.easypeasyrpc.exception.RpcErrorException;
import com.github.dadekuma.easypeasyrpc.exception.message.GenericExceptionMessage;
import com.github.dadekuma.easypeasyrpc.resource.RpcCommunicator;
import com.github.dadekuma.easypeasyrpc.resource.RpcResponse;
import com.github.dadekuma.easypeasyrpc.resource.RpcRequest;
import com.github.dadekuma.easypeasyrpc.resource.params.RpcParameterList;
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
                .registerTypeAdapter(RpcRequest.class, new RequestSerializer())
                .create();
    }

    public RpcResponse fulfillRequest(String methodName, RpcParameterList params) throws RpcErrorException, TimeoutException {
        try {
            RpcRequest request = new RpcRequest(methodName, params, requestNumber.toString(), RpcManager.RPC_VERSION);
            sendRequest(request);
            if (!request.isNotification()) {
                return receiveResponse();
            }
        } catch (RpcCommunicatorException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void sendRequest(RpcRequest request) throws RpcCommunicatorException {
        try {
            ++requestNumber;
            String stringRequest = gson.toJson(request);
            communicator.sendMsg(stringRequest);
        } catch (NullPointerException e) {
            throw new RpcCommunicatorException(GenericExceptionMessage.INVALID_COMMUNICATOR.toString());
        }
    }


    private RpcResponse receiveResponse() throws RpcCommunicatorException, RpcErrorException, TimeoutException {
        try {
            String stringResponse = communicator.receiveMsg();
            RpcResponse response = gson.fromJson(stringResponse, RpcResponse.class);
            if (response.getError() != null)
                throw new RpcErrorException(response.getError());
            return response;
        } catch (TimeoutException e) {
            communicator.dispose();
            throw e;
        } catch (NullPointerException e) {
            throw new RpcCommunicatorException(GenericExceptionMessage.INVALID_COMMUNICATOR.toString());
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
