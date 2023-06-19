package com.yuriytkach.demo.demospringsecurity.security;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yuriytkach.demo.demospringsecurity.jwt.JwtAuthenticationFilter;
import com.yuriytkach.demo.demospringsecurity.jwt.JwtLoginFilter;
import com.yuriytkach.demo.demospringsecurity.jwt.JwtProperties;
import com.yuriytkach.demo.demospringsecurity.jwt.JwtService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
@EnableConfigurationProperties({ SecurityProperties.class, JwtProperties.class })
public class SecurityConfig {

  private final ApiKeySimpleFilter simpleApiKeyAuthFilter;
  private final ApiKeyDbFilter apiKeyDbFilter;
  private final JwtAuthenticationFilter jwtAuthenticationFilter;

  @Bean
  @Order(1)
  SecurityFilterChain securityFilterChain1(final HttpSecurity http, final JwtLoginFilter jwtLoginFilter)
    throws Exception {
    http
      .csrf(AbstractHttpConfigurer::disable)
      .authorizeHttpRequests((requests) -> requests
        .requestMatchers(new AntPathRequestMatcher("/forex/currencies")).permitAll()
        .requestMatchers(new AntPathRequestMatcher("/auth")).permitAll()
        .anyRequest().authenticated()
      )
      .addFilterAfter(simpleApiKeyAuthFilter, LogoutFilter.class)
      .addFilterAfter(apiKeyDbFilter, ApiKeySimpleFilter.class)
      .addFilterAfter(jwtAuthenticationFilter, ApiKeyDbFilter.class)
      .addFilterAfter(jwtLoginFilter, ApiKeyDbFilter.class)
      .sessionManagement(config -> config.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

    return http.build();
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public JwtLoginFilter jwtLoginFilter(
    final ObjectMapper objectMapper,
    final AuthenticationConfiguration config,
    final JwtService jwtService
  ) throws Exception {
    final JwtLoginFilter filter = new JwtLoginFilter(objectMapper, jwtService);
    filter.setAuthenticationManager(config.getAuthenticationManager());
    return filter;
  }

}
