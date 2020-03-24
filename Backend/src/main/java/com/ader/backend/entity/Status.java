package com.ader.backend.entity;

public enum Status {
    ACTIVE("ACTIVE"), DELETED("DELETED");

    private final String name;

    Status(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
