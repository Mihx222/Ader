package com.ader.backend.entity;

public enum BidStatus {

  NEW("NEW"),
  ACCEPTED("ACCEPTED"),
  DECLINED("DECLINED"),
  APPROVED("APPROVED"),
  CANCELED("CANCELED");

  private final String name;
  private static final BidStatus[] values = values();

  BidStatus(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public static BidStatus[] getValues() {
    return values;
  }
}
