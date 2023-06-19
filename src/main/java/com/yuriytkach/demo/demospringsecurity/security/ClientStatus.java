package com.yuriytkach.demo.demospringsecurity.security;

import java.util.Arrays;
import java.util.Optional;

public enum ClientStatus {
  REGULAR,
  PAID,
  VIP;

  public static Optional<ClientStatus> fromString(final String status) {
    return Arrays.stream(ClientStatus.values())
      .filter(clientStatus -> clientStatus.name().equalsIgnoreCase(status))
      .findFirst();
  }
}
