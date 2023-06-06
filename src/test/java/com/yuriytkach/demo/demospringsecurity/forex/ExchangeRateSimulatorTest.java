package com.yuriytkach.demo.demospringsecurity.forex;

import static java.util.function.Predicate.not;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.withinPercentage;
import static org.mockito.Mockito.lenient;

import java.util.Arrays;
import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ExchangeRateSimulatorTest {

  private static final double DEVIATION_PERCENT = 10.0;
  @Mock
  private ExchangeRateProperties exchangeRateProperties;

  @InjectMocks
  private ExchangeRateSimulator tested;

  @BeforeEach
  void setupDeviation() {
    lenient().when(exchangeRateProperties.getDeviationPercentage()).thenReturn(DEVIATION_PERCENT);
  }

  @ParameterizedTest
  @EnumSource(Currency.class)
  void shouldReturnRandomRateForCurrencies(final Currency from) {
    Arrays.stream(Currency.values())
      .filter(not(from::equals))
      .forEach(to -> {
        final double exchangeRate = tested.getExchangeRate(from, to);
        assertThat(exchangeRate).isPositive();
      });
  }

  @ParameterizedTest
  @EnumSource(Currency.class)
  void shouldReturnRandomRateWithinDeviationForSubsequentRequests(final Currency from) {
    Arrays.stream(Currency.values())
      .filter(not(from::equals))
      .forEach(to -> {
        final double exchangeRate1 = tested.getExchangeRate(from, to);

        IntStream.range(1, 3)
          .forEach(ignored -> {
            final double exchangeRateSubsequent = tested.getExchangeRate(from, to);
            assertThat(exchangeRateSubsequent).isCloseTo(exchangeRate1, withinPercentage(DEVIATION_PERCENT));
          });
      });
  }

  @ParameterizedTest
  @EnumSource(Currency.class)
  void shouldReturnReciprocalRateForCurrencies(final Currency from) {
    Arrays.stream(Currency.values())
      .filter(not(from::equals))
      .forEach(to -> {
        final double exchangeRate = tested.getExchangeRate(from, to);
        final double reciprocalExchangeRate = tested.getExchangeRate(to, from);
        assertThat(exchangeRate).isPositive();
        assertThat(reciprocalExchangeRate).isPositive();
        assertThat(exchangeRate * reciprocalExchangeRate).isCloseTo(1.0, withinPercentage(0.0001));
      });
  }

  @ParameterizedTest
  @EnumSource(Currency.class)
  void shouldReturnOneForSameCurrencies(final Currency from) {
    final double exchangeRate = tested.getExchangeRate(from, from);
    assertThat(exchangeRate).isCloseTo(1.0, withinPercentage(0.0001));
  }

}
