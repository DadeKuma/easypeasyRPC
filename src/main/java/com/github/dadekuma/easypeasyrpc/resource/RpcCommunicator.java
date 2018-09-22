package com.github.dadekuma.easypeasyrpc.resource;

import java.util.concurrent.TimeoutException;

public interface RpcCommunicator {
    void sendMsg(String msg);
    String receiveMsg() throws TimeoutException;
    void dispose();
}
