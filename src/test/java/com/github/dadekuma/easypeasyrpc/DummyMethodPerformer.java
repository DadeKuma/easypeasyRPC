package com.github.dadekuma.easypeasyrpc;

import com.github.dadekuma.easypeasyrpc.exception.ParameterOutOfBoundException;
import com.github.dadekuma.easypeasyrpc.resource.method.RpcMethodList;
import com.github.dadekuma.easypeasyrpc.resource.method.RpcMethodPerformer;
import com.github.dadekuma.easypeasyrpc.resource.params.RpcElement;
import com.github.dadekuma.easypeasyrpc.resource.params.RpcParameterList;

public class DummyMethodPerformer implements RpcMethodPerformer {
    private RpcMethodList methodList;

    public DummyMethodPerformer() {
        methodList = new RpcMethodList();
        methodList.addMethod("return1")
                .addMethod("returnBiggest", new Integer[]{2, 3}) //test with overloading
                .addMethod("returnMyParam", 1)
                .addMethod("operateClass", 1);
    }

    @Override
    public RpcElement perform(String methodName, RpcParameterList params) throws IllegalArgumentException, ParameterOutOfBoundException {
        if(methodName.equals("return1")){
            return return1();
        }
        else if(methodName.equals("returnBiggest")){
            if(params.getParameters().size() == 2){
                int parameter1 = params.getParameterByPosition(0).getAsInt();
                int parameter2 = params.getParameterByPosition(1).getAsInt();
                return returnBiggest(parameter1, parameter2);
            }
            else{
                int parameter1 = params.getParameterByPosition(0).getAsInt();
                int parameter2 = params.getParameterByPosition(1).getAsInt();
                int parameter3 = params.getParameterByPosition(2).getAsInt();
                return returnBiggest(parameter1, parameter2, parameter3);
            }

        }
        else if(methodName.equals("returnMyParam")){
            RpcElement p1 = params.getParameterByPosition(0);
            return returnMyParam(p1);
        }

        else if(methodName.equals("operateClass")){
            DummyClass dummy = params.getParameterByPosition(0).getAsClass(DummyClass.class);
            return operateClass(dummy);
        }

        return null;
    }

    RpcElement return1(){
        return new RpcElement(1);
    }

    RpcElement returnBiggest(int p1, int p2){
        if(p1 > p2)
            return new RpcElement(p1);
        return new RpcElement(p2);
    }
    RpcElement returnBiggest(int p1, int p2, int p3){
        int max = p1;
        if(p2 > max)
            max = p2;
        if(p3 > max)
            max = p3;
        return new RpcElement(max);
    }

    RpcElement returnMyParam(RpcElement p1){
        return p1;
    }

    RpcElement operateClass(DummyClass p1){
        p1.exampleFunction();
        return new RpcElement(p1.getExample());
    }

    public RpcMethodList getMethodList() {
        return methodList;
    }
}
