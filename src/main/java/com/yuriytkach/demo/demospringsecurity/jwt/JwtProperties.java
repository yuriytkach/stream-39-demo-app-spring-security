package com.yuriytkach.demo.demospringsecurity.jwt;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "app.jwt")
public class JwtProperties {

  private String signingSecret;
  private Duration tokenDuration;

}
