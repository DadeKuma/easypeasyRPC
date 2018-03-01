package com.github.dadekuma.easypeasyrpc;

import com.github.dadekuma.easypeasyrpc.exception.ParameterOutOfBoundException;
import com.github.dadekuma.easypeasyrpc.resource.method.MethodList;
import com.github.dadekuma.easypeasyrpc.resource.method.MethodPerformer;
import com.github.dadekuma.easypeasyrpc.resource.params.Element;
import com.github.dadekuma.easypeasyrpc.resource.params.ParameterList;

public class DummyMethodPerformer implements MethodPerformer{
    private MethodList methodList;

    public DummyMethodPerformer() {
        methodList = new MethodList();
        methodList.addMethod("return1")
                .addMethod("returnBiggest", new Integer[]{2, 3}) //test with overloading
                .addMethod("returnMyParam", 1)
                .addMethod("operateClass", 1);
    }

    @Override
    public Element perform(String methodName, ParameterList params) throws IllegalArgumentException, ParameterOutOfBoundException {
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
            Element p1 = params.getParameterByPosition(0);
            return returnMyParam(p1);
        }

        else if(methodName.equals("operateClass")){
            DummyClass dummy = params.getParameterByPosition(0).getAsClass(DummyClass.class);
            return operateClass(dummy);
        }

        return null;
    }

    Element return1(){
        return new Element(1);
    }

    Element returnBiggest(int p1, int p2){
        if(p1 > p2)
            return new Element(p1);
        return new Element(p2);
    }
    Element returnBiggest(int p1, int p2, int p3){
        int max = p1;
        if(p2 > max)
            max = p2;
        if(p3 > max)
            max = p3;
        return new Element(max);
    }

    Element returnMyParam(Element p1){
        return p1;
    }

    Element operateClass(DummyClass p1){
        p1.exampleFunction();
        return new Element(p1.getExample());
    }

    public MethodList getMethodList() {
        return methodList;
    }
}
