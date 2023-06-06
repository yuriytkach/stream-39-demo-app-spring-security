package com.yuriytkach.demo.demospringsecurity.security;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.yuriytkach.demo.demospringsecurity.forex.Currency;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "app.security")
public class SecurityProperties {
  private Map<ClientStatus, String> statusCurrencies;

  public Map<ClientStatus, Set<Currency>> getStatusCurrencies() {
    return statusCurrencies.entrySet().stream()
      .map(entry -> Map.entry(entry.getKey(), Arrays.stream(entry.getValue().split(","))
        .map(String::strip)
        .map(Currency::valueOf)
        .collect(Collectors.toUnmodifiableSet())))
      .collect(Collectors.toUnmodifiableMap(Map.Entry::getKey, Map.Entry::getValue));
  }
}
