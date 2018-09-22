package com.github.dadekuma.easypeasyrpc;

import com.github.dadekuma.easypeasyrpc.resource.RpcCommunicator;

public class DummyCommunicator implements RpcCommunicator {
    public String sentMessage;

    @Override
    public void sendMsg(String msg) {
        sentMessage = msg;
    }

    @Override
    public String receiveMsg(){
        return sentMessage;
    }

    @Override
    public void dispose() {
        sentMessage = null;
    }
}
