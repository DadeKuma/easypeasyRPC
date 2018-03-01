package com.github.dadekuma.easypeasyrpc.resource.method;

import com.github.dadekuma.easypeasyrpc.exception.ParameterOutOfBoundException;
import com.github.dadekuma.easypeasyrpc.exception.RPCException;
import com.github.dadekuma.easypeasyrpc.resource.params.Element;
import com.github.dadekuma.easypeasyrpc.resource.params.ParameterList;

public interface MethodPerformer {
    Element perform(String methodName, ParameterList params) throws IllegalArgumentException, RPCException, ParameterOutOfBoundException;
}