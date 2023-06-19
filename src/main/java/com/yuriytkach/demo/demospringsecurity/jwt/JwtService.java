package com.yuriytkach.demo.demospringsecurity.jwt;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtService {

  private final JwtProperties jwtProperties;

  String generateToken(final Authentication authentication) {
    log.debug("Generating JWT for user: {}", authentication.getName());

    return JWT.create()
      .withSubject(authentication.getName())
      .withExpiresAt(Date.from(Instant.now().plus(jwtProperties.getTokenDuration())))
      .withNotBefore(Date.from(Instant.now()))
      .withIssuedAt(Date.from(Instant.now()))
      .withIssuer("demo-spring-security-app")
      .withClaim("role", authentication.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority).findFirst().orElseThrow())
      .sign(Algorithm.HMAC512(jwtProperties.getSigningSecret()));
  }

  Optional<ParsedJwt> parseToken(final String jwtToken) {
    try {
      final DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(jwtProperties.getSigningSecret()))
        .acceptExpiresAt(10) // seconds to allow for clock skew
        .build()
        .verify(jwtToken);

      final ParsedJwt parsedJwt = new ParsedJwt(decodedJWT.getSubject(), decodedJWT.getClaim("role").asString());
      return Optional.of(parsedJwt);
    } catch (final JWTVerificationException ex) {
      log.debug("Invalid JWT token: {}", ex.getMessage());
      return Optional.empty();
    }
  }

  record ParsedJwt(String username, String role) { }
}
