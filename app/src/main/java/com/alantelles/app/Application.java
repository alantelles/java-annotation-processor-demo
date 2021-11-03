package com.alantelles.app;

import com.alantelles.annotations.Bright;

@Bright
public class Application {

    public static void main(String[] args) {

        ExtraImpl extra = new ExtraImpl();
        extra.setName("processor");
        System.out.println(extra.getName());

        System.out.println("Hello");
    }

}
