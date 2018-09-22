package com.github.dadekuma.easypeasyrpc.resource.method;

import com.github.dadekuma.easypeasyrpc.exception.ParameterOutOfBoundException;
import com.github.dadekuma.easypeasyrpc.exception.RpcException;
import com.github.dadekuma.easypeasyrpc.resource.params.RpcElement;
import com.github.dadekuma.easypeasyrpc.resource.params.RpcParameterList;

public interface RpcMethodPerformer {
    RpcElement perform(String methodName, RpcParameterList params) throws IllegalArgumentException, RpcException, ParameterOutOfBoundException;
}
