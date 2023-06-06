package com.yuriytkach.demo.demospringsecurity.forex;


import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/forex")
@RequiredArgsConstructor
public class ExchangeRateController {

  private final ExchangeRateService exchangeRateService;

  @GetMapping
  @PreAuthorize("@statusCurrencySecurityEvaluator.hasAccessToCurrencies(authentication, #from, #to)")
  public ResponseEntity<ExchangeRate> exchangeRate(
    final Currency from,
    final Currency to
  ) {
    return ResponseEntity.ok(exchangeRateService.exchangeRate(from, to));
  }

  @GetMapping("/currencies")
  public ResponseEntity<List<Currency>> allCurrencies() {
    return ResponseEntity.ok(List.of(Currency.values()));
  }

}
