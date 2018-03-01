package com.github.dadekuma.easypeasyrpc;

import com.github.dadekuma.easypeasyrpc.exception.CommunicatorException;
import com.github.dadekuma.easypeasyrpc.exception.ErrorException;
import com.github.dadekuma.easypeasyrpc.exception.message.GenericExceptionMessage;
import com.github.dadekuma.easypeasyrpc.resource.Communicator;
import com.github.dadekuma.easypeasyrpc.resource.Request;
import com.github.dadekuma.easypeasyrpc.resource.Response;
import com.github.dadekuma.easypeasyrpc.resource.params.ParameterList;
import com.github.dadekuma.easypeasyrpc.serialization.RequestSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeoutException;

public abstract class Client {

    private final Gson gson;
    private Communicator communicator;
    private Integer requestNumber;

    public Client() {
        this(null);
    }

    public Client(Communicator communicator) {
        this.communicator = communicator;
        requestNumber = 0;
        gson = new GsonBuilder()
                .registerTypeAdapter(Request.class, new RequestSerializer())
                .create();
    }

    public Response fulfillRequest(String methodName, ParameterList params) throws ErrorException, TimeoutException {
        try {
            Request request = new Request(methodName, params, requestNumber.toString(), JsonRPCManager.RPC_VERSION);
            sendRequest(request);
            if (!request.isNotification()) {
                return receiveResponse();
            }
        } catch (CommunicatorException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void sendRequest(Request request) throws CommunicatorException {
        try {
            ++requestNumber;
            String stringRequest = gson.toJson(request);
            communicator.sendMsg(stringRequest);
        } catch (NullPointerException e) {
            throw new CommunicatorException(GenericExceptionMessage.INVALID_COMMUNICATOR.toString());
        }
    }


    private Response receiveResponse() throws CommunicatorException, ErrorException, TimeoutException {
        try {
            String stringResponse = communicator.receiveMsg();
            Response response = gson.fromJson(stringResponse, Response.class);
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

    public void setCommunicator(Communicator communicator) {
        this.communicator = communicator;
    }

    public Communicator getCommunicator() {
        return communicator;
    }

    public Gson getGson() {
        return gson;
    }
}
