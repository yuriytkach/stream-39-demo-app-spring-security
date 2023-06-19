package com.yuriytkach.demo.demospringsecurity.jwt;

import java.io.IOException;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtLoginFilter extends AbstractAuthenticationProcessingFilter {

  private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER =
    new AntPathRequestMatcher("/auth", "POST");

  private final ObjectMapper objectMapper;
  private final JwtService jwtService;

  public JwtLoginFilter(final ObjectMapper objectMapper, final JwtService jwtService) {
    super(DEFAULT_ANT_PATH_REQUEST_MATCHER);
    this.objectMapper = objectMapper;
    this.jwtService = jwtService;
  }

  @Override
  public Authentication attemptAuthentication(final HttpServletRequest request, final HttpServletResponse response)
    throws AuthenticationException {
    try {
      log.debug("Attempting authentication...");
      final var auth = objectMapper.readValue(request.getInputStream(), AuthDto.class);
      log.debug("Read user auth: {}", auth.username());

      final var authRequest = UsernamePasswordAuthenticationToken.unauthenticated(auth.username(), auth.password());

      authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));

      return this.getAuthenticationManager().authenticate(authRequest);
    } catch (final IOException ex) {
      throw new AuthenticationServiceException("Failed to parse authentication request body", ex);
    }
  }

  @Override
  protected void successfulAuthentication(
    final HttpServletRequest request,
    final HttpServletResponse response,
    final FilterChain chain,
    final Authentication authentication
  ) throws IOException {

    final String token = jwtService.generateToken(authentication);

    response.setStatus(HttpServletResponse.SC_OK);
    response.setContentType("application/json");
    response.getWriter().write(objectMapper.writeValueAsString(new TokenDto(token)));
  }
}
