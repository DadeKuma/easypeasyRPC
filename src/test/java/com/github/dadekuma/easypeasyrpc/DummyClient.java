package com.github.dadekuma.easypeasyrpc;

import com.github.dadekuma.easypeasyrpc.resource.RpcCommunicator;

public class DummyClient extends RpcClient {
    public DummyClient() {
    }

    public DummyClient(RpcCommunicator communicator) {
        super(communicator);
    }
}
