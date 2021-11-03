package com.alantelles.enums;

public enum ServiceType {


    SERV1("Servico1"),
    SERV2("Servico2");

    private String name;

    ServiceType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
