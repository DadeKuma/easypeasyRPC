package com.github.dadekuma.easypeasyrpc;

import com.github.dadekuma.easypeasyrpc.resource.Communicator;

public class DummyClient extends Client {
    public DummyClient() {
    }

    public DummyClient(Communicator communicator) {
        super(communicator);
    }
}
