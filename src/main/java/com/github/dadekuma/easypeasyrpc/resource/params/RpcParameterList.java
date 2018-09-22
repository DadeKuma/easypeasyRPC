package com.github.dadekuma.easypeasyrpc.resource.params;

import com.github.dadekuma.easypeasyrpc.exception.ParameterOutOfBoundException;
import com.github.dadekuma.easypeasyrpc.exception.message.GenericExceptionMessage;

public class RpcParameterList {
    private final RpcElement parameters;

    public RpcParameterList(RpcElement parameter){
        this.parameters = parameter;
    }

    public RpcElement getParameterByPosition(int index) throws ParameterOutOfBoundException {
        if((parameters.isJsonObject() || parameters.isJsonPrimitive()) && index != 0)
            throw new ParameterOutOfBoundException(GenericExceptionMessage.BAD_INDEX.toString());
        else if(parameters.isJsonArray() && (index < 0 || index > parameters.getAsJsonArray().size()))
            throw new ParameterOutOfBoundException(GenericExceptionMessage.BAD_INDEX.toString());
        if(parameters.isJsonArray())
            return new RpcElement(parameters.getAsJsonArray().get(index));
        return parameters;
    }

    public RpcElement getParameters(){
        return parameters;
    }
}
