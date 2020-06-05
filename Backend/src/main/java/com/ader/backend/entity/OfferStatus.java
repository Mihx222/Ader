package com.ader.backend.entity;

public enum OfferStatus {
    OPEN("OPEN"),
    EXPIRED("EXPIRED"),
    ASSIGNED("ASSIGNED"),
    IN_PROGRESS("IN PROGRESS"),
    COMPLETED("COMPLETED"),
    CLOSED("CLOSED");

    private final String name;
    private final static OfferStatus[] values = values();

    OfferStatus(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public String getName() {
        return name;
    }

    public static OfferStatus[] getValues() {
        return values;
    }
}
