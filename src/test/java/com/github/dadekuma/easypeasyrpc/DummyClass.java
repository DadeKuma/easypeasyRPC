package com.github.dadekuma.easypeasyrpc;

public class DummyClass {
    private float example;

    public DummyClass(float example) {
        this.example = example;
    }

    public void exampleFunction(){
        example+=1;
    }

    public float getExample() {
        return example;
    }
}
