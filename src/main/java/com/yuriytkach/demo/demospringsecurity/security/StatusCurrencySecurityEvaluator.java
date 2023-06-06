package com.yuriytkach.demo.demospringsecurity.security;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.yuriytkach.demo.demospringsecurity.forex.Currency;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class StatusCurrencySecurityEvaluator {

  private final SecurityProperties securityProperties;

  public boolean hasAccessToCurrencies(final Authentication authentication, final Currency from, final Currency to) {
    return authentication.getAuthorities().stream()
      .map(authority -> ClientStatus.valueOf(authority.getAuthority()))
      .map(securityProperties.getStatusCurrencies()::get)
      .anyMatch(currencies -> currencies.contains(from) && currencies.contains(to));
  }

}
