package com.github.dadekuma.easypeasyrpc.resource.params;

import com.github.dadekuma.easypeasyrpc.exception.ParameterOutOfBoundException;
import com.github.dadekuma.easypeasyrpc.exception.message.GenericExceptionMessage;

public class ParameterList {
    private final Element parameters;

    public ParameterList(Element parameter){
        this.parameters = parameter;
    }

    public Element getParameterByPosition(int index) throws ParameterOutOfBoundException {
        if((parameters.isJsonObject() || parameters.isJsonPrimitive()) && index != 0)
            throw new ParameterOutOfBoundException(GenericExceptionMessage.BAD_INDEX.toString());
        else if(parameters.isJsonArray() && (index < 0 || index > parameters.getAsJsonArray().size()))
            throw new ParameterOutOfBoundException(GenericExceptionMessage.BAD_INDEX.toString());
        if(parameters.isJsonArray())
            return new Element(parameters.getAsJsonArray().get(index));
        return parameters;
    }

    public Element getParameters(){
        return parameters;
    }
}
