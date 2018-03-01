package com.github.dadekuma.easypeasyrpc.resource;

import java.util.concurrent.TimeoutException;

public interface Communicator {
    void sendMsg(String msg);
    String receiveMsg() throws TimeoutException;
    void dispose();
}
