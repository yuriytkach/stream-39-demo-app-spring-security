package com.yuriytkach.demo.demospringsecurity.forex;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "app.exchange-rate")
class ExchangeRateProperties {
  private double deviationPercentage;
}
