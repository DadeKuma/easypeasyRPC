package com.github.dadekuma.easypeasyrpc;

import com.github.dadekuma.easypeasyrpc.exception.RpcCommunicatorException;
import com.github.dadekuma.easypeasyrpc.exception.ParameterOutOfBoundException;
import com.github.dadekuma.easypeasyrpc.exception.RpcException;
import com.github.dadekuma.easypeasyrpc.exception.message.GenericExceptionMessage;
import com.github.dadekuma.easypeasyrpc.resource.RpcCommunicator;
import com.github.dadekuma.easypeasyrpc.resource.method.RpcMethodPerformer;
import com.github.dadekuma.easypeasyrpc.resource.params.RpcElement;
import com.github.dadekuma.easypeasyrpc.resource.params.RpcParameterList;

import java.util.concurrent.TimeoutException;

public abstract class RpcServer implements RpcMethodPerformer {
    private RpcCommunicator communicator;
    private RpcManager jsonRPCManager;
    private boolean isRunning = true;

    public RpcServer(){}

    public RpcServer(RpcCommunicator communicator, RpcManager jsonRPCManager) {
        this.communicator = communicator;
        this.jsonRPCManager = jsonRPCManager;
    }

    @Override
    public abstract RpcElement perform(String methodName, RpcParameterList params) throws IllegalArgumentException, RpcException, ParameterOutOfBoundException;

    public void runServer() throws RpcCommunicatorException {
        if(communicator == null || jsonRPCManager == null)
            throw new RpcCommunicatorException(GenericExceptionMessage.INVALID_COMMUNICATOR.toString());
        try {
            while(isRunning){
                String stringRequest = receiveRequest();
                String stringResponse = jsonRPCManager.buildResponseToString(stringRequest);
                sendResponse(stringResponse);
            }
        }
        catch (TimeoutException e){
            e.printStackTrace();
        }
    }

    public String receiveRequest() throws RpcCommunicatorException, TimeoutException{
        try {
            return communicator.receiveMsg();
        }
        catch(NullPointerException e){
            throw new RpcCommunicatorException(GenericExceptionMessage.INVALID_COMMUNICATOR.toString());
        }
    }

    public void sendResponse(String response) throws RpcCommunicatorException {
        if(response != null && !response.isEmpty()) {
            try {
                communicator.sendMsg(response);
            }
            catch (NullPointerException e) {
                throw new RpcCommunicatorException(GenericExceptionMessage.INVALID_COMMUNICATOR.toString());
            }
        }
    }

    public void setCommunicator(RpcCommunicator communicator) {
        this.communicator = communicator;
    }

    public void setJsonRPCManager(RpcManager jsonRPCManager) {
        this.jsonRPCManager = jsonRPCManager;
    }

    public RpcManager getJsonRPCManager() {
        return jsonRPCManager;
    }

    public RpcCommunicator getCommunicator() {
        return communicator;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }
}
