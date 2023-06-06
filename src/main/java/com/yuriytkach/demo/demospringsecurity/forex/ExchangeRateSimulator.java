package com.yuriytkach.demo.demospringsecurity.forex;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
class ExchangeRateSimulator {

  private final ExchangeRateProperties properties;
  private final Map<String, Double> exchangeRates = new HashMap<>();
  private final SecureRandom random = new SecureRandom();

  public double getExchangeRate(final Currency currency1, final Currency currency2) {
    if (currency1 == currency2) {
      return 1.0; // Same currency, exchange rate is always 1
    }

    final String pair = currency1.name() + "_" + currency2.name();
    final String reciprocalPair = currency2.name() + "_" + currency1.name();

    final double rate;
    if (exchangeRates.containsKey(pair)) {
      final double previousRate = exchangeRates.get(pair);
      rate = getRandomDeviationRate(previousRate);
    } else if (exchangeRates.containsKey(reciprocalPair)) {
      return 1.0 / exchangeRates.get(reciprocalPair);
    } else {
      rate = getRandomRate();
      exchangeRates.put(pair, rate);
    }

    return rate;
  }

  private double getRandomRate() {
    return random.nextDouble(30.0);
  }

  private double getRandomDeviationRate(final double rate) {
    final double deviation = rate * properties.getDeviationPercentage() * 0.01;
    final double min = rate - deviation;
    final double max = rate + deviation;
    return random.nextDouble(min, max);
  }
}

