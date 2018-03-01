package com.github.dadekuma.easypeasyrpc.resource.method;

import com.github.dadekuma.easypeasyrpc.exception.message.GenericExceptionMessage;
import com.github.dadekuma.easypeasyrpc.exception.message.RPCExceptionMessage;

import java.util.HashMap;
import java.util.Map;

public class MethodList {
    private Map<String, Integer[]> methods;

    public MethodList() {
        this(new HashMap<String, Integer[]>());
    }

    public MethodList(Map<String, Integer[]> methods) {
        this.methods = methods;
    }

    public MethodList(MethodList methodList){
        methods = methodList.methods;
    }

    public MethodList addMethod(String methodName){
        return addMethod(methodName, 0);
    }

    public MethodList addMethod(String methodName, int numberOfParameters){
        return addMethod(methodName, new Integer[]{numberOfParameters});
    }

    public MethodList addMethod(String methodName, Integer[] numberOfParameters){
        if (methodName.startsWith("rpc.")) {
            throw new IllegalArgumentException(RPCExceptionMessage.BAD_METHOD_NAME.toString());
        }
        for(Integer i : numberOfParameters){
            if(i < 0)
                throw new IllegalArgumentException(GenericExceptionMessage.NEGATIVE_NUMBER_OF_PARAMETERS.toString());
        }
        methods.put(methodName, numberOfParameters);
        return this;
    }

    public MethodList deleteMethod(String methodName){
        methods.remove(methodName);
        return this;
    }

    public MethodList deleteAll(){
        methods.clear();
        return this;
    }

    public Map<String, Integer[]> getMethods() {
        return methods;
    }
}
