package com.ader.backend.entity;

public enum OfferStatus {
    OPEN("OPEN"),
    CLOSED("CLOSED");

    private final String name;

    OfferStatus(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
