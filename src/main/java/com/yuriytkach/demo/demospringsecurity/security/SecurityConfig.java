package com.yuriytkach.demo.demospringsecurity.security;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.logout.LogoutFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
@EnableConfigurationProperties(SecurityProperties.class)
public class SecurityConfig {

  private final ApiKeySimpleFilter simpleApiKeyAuthFilter;
  private final ApiKeyDbFilter apiKeyDbFilter;

  @Bean
  SecurityFilterChain securityFilterChain1(final HttpSecurity http) throws Exception {
    http.authorizeHttpRequests((requests) -> requests
      .requestMatchers("/forex/currencies").permitAll()
      .anyRequest().authenticated()
    )
      .addFilterAfter(simpleApiKeyAuthFilter, LogoutFilter.class)
      .addFilterAfter(apiKeyDbFilter, ApiKeySimpleFilter.class)
      .exceptionHandling((exceptionHandling) -> exceptionHandling
        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
      )
      .sessionManagement(config -> config.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
    return http.build();
  }

}
