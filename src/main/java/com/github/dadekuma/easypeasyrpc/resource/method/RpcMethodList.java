package com.github.dadekuma.easypeasyrpc.resource.method;

import com.github.dadekuma.easypeasyrpc.exception.message.GenericExceptionMessage;
import com.github.dadekuma.easypeasyrpc.exception.message.RpcExceptionMessage;

import java.util.HashMap;
import java.util.Map;

public class RpcMethodList {
    private Map<String, Integer[]> methods;

    public RpcMethodList() {
        this(new HashMap<String, Integer[]>());
    }

    public RpcMethodList(Map<String, Integer[]> methods) {
        this.methods = methods;
    }

    public RpcMethodList(RpcMethodList methodList){
        methods = methodList.methods;
    }

    public RpcMethodList addMethod(String methodName){
        return addMethod(methodName, 0);
    }

    public RpcMethodList addMethod(String methodName, int numberOfParameters){
        return addMethod(methodName, new Integer[]{numberOfParameters});
    }

    public RpcMethodList addMethod(String methodName, Integer[] numberOfParameters){
        if (methodName.startsWith("rpc.")) {
            throw new IllegalArgumentException(RpcExceptionMessage.BAD_METHOD_NAME.toString());
        }
        for(Integer i : numberOfParameters){
            if(i < 0)
                throw new IllegalArgumentException(GenericExceptionMessage.NEGATIVE_NUMBER_OF_PARAMETERS.toString());
        }
        methods.put(methodName, numberOfParameters);
        return this;
    }

    public RpcMethodList deleteMethod(String methodName){
        methods.remove(methodName);
        return this;
    }

    public RpcMethodList deleteAll(){
        methods.clear();
        return this;
    }

    public Map<String, Integer[]> getMethods() {
        return methods;
    }
}
