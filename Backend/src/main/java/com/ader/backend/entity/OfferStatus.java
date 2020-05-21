package com.ader.backend.entity;

public enum OfferStatus {
    OPEN("OPEN"),
    EXPIRED("EXPIRED"),
    ASSIGNED("ASSIGNED"),
    COMPLETED("COMPLETED"),
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
