package com.yuriytkach.demo.demospringsecurity.security;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ApiKeySimpleFilter extends OncePerRequestFilter {

  private static final String HEADER_X_API_KEY = "x-api-key";

  // Extract to properties or something :)
  private static final String VALID_KEY = "valid-key";

  @Override
  protected void doFilterInternal(
    final HttpServletRequest request,
    final HttpServletResponse response,
    final FilterChain filterChain
  ) throws ServletException, IOException {

    final String apiKey = request.getHeader(HEADER_X_API_KEY);

    if (apiKey == null || apiKey.isBlank()) {
      filterChain.doFilter(request, response);
      return;
    }

    if (VALID_KEY.equals(apiKey)) {
      log.info("Valid API KEY provided");

      SecurityContextHolder.getContext().setAuthentication(
       new UsernamePasswordAuthenticationToken("user", null,
         List.of(new SimpleGrantedAuthority(ClientStatus.REGULAR.name())))
      );

      filterChain.doFilter(request, response);
    } else {
      log.debug("Invalid API KEY provided ");
      throw new BadCredentialsException("Invalid API KEY");
    }
  }
}
