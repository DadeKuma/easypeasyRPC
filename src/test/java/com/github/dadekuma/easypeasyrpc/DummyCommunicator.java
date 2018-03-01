package com.github.dadekuma.easypeasyrpc;

import com.github.dadekuma.easypeasyrpc.resource.Communicator;

public class DummyCommunicator implements Communicator {
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
