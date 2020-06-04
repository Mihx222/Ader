package com.ader.backend.entity;

public enum BidStatus {

  NEW("NEW"),
  ACCEPTED("ACCEPTED"),
  DECLINED("DECLINED");

  private final String name;

  BidStatus(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
