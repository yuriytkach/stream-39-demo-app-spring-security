package com.yuriytkach.demo.demospringsecurity.forex;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExchangeRate {
  private final Currency from;
  private final Currency to;
  private final double rate;
}
