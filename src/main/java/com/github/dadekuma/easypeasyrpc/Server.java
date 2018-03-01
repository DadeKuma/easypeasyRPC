package com.github.dadekuma.easypeasyrpc;

import com.github.dadekuma.easypeasyrpc.exception.CommunicatorException;
import com.github.dadekuma.easypeasyrpc.exception.ParameterOutOfBoundException;
import com.github.dadekuma.easypeasyrpc.exception.RPCException;
import com.github.dadekuma.easypeasyrpc.exception.message.GenericExceptionMessage;
import com.github.dadekuma.easypeasyrpc.resource.Communicator;
import com.github.dadekuma.easypeasyrpc.resource.method.MethodPerformer;
import com.github.dadekuma.easypeasyrpc.resource.params.Element;
import com.github.dadekuma.easypeasyrpc.resource.params.ParameterList;

import java.util.concurrent.TimeoutException;

public abstract class Server implements MethodPerformer {
    private Communicator communicator;
    private JsonRPCManager jsonRPCManager;
    private boolean isRunning = true;

    public Server(){}

    public Server(Communicator communicator, JsonRPCManager jsonRPCManager) {
        this.communicator = communicator;
        this.jsonRPCManager = jsonRPCManager;
    }

    @Override
    public abstract Element perform(String methodName, ParameterList params) throws IllegalArgumentException, RPCException, ParameterOutOfBoundException;

    public void runServer() throws CommunicatorException {
        if(communicator == null || jsonRPCManager == null)
            throw new CommunicatorException(GenericExceptionMessage.INVALID_COMMUNICATOR.toString());
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

    public String receiveRequest() throws CommunicatorException, TimeoutException{
        try {
            return communicator.receiveMsg();
        }
        catch(NullPointerException e){
            throw new CommunicatorException(GenericExceptionMessage.INVALID_COMMUNICATOR.toString());
        }
    }

    public void sendResponse(String response) throws CommunicatorException{
        if(response != null && !response.isEmpty()) {
            try {
                communicator.sendMsg(response);
            }
            catch (NullPointerException e) {
                throw new CommunicatorException(GenericExceptionMessage.INVALID_COMMUNICATOR.toString());
            }
        }
    }

    public void setCommunicator(Communicator communicator) {
        this.communicator = communicator;
    }

    public void setJsonRPCManager(JsonRPCManager jsonRPCManager) {
        this.jsonRPCManager = jsonRPCManager;
    }

    public JsonRPCManager getJsonRPCManager() {
        return jsonRPCManager;
    }

    public Communicator getCommunicator() {
        return communicator;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }
}
