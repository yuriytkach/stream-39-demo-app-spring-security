package com.yuriytkach.demo.demospringsecurity.security;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.yuriytkach.demo.demospringsecurity.apikey.ApiKeyRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApiKeyDbFilter extends OncePerRequestFilter {

  private static final String HEADER_X_CLIENT_API_KEY = "x-client-api-key";

  private final ApiKeyRepository apiKeyRepository;

  @Override
  protected void doFilterInternal(
    final HttpServletRequest request,
    final HttpServletResponse response,
    final FilterChain filterChain
  ) throws ServletException, IOException {

    if (SecurityContextHolder.getContext().getAuthentication() != null) {
      log.debug("SecurityContextHolder already contains authentication: '{}'",
        SecurityContextHolder.getContext().getAuthentication());
      filterChain.doFilter(request, response);
      return;
    }

    final String apiKey = request.getHeader(HEADER_X_CLIENT_API_KEY);

    if (apiKey == null || apiKey.isBlank()) {
      filterChain.doFilter(request, response);
      return;
    }

    apiKeyRepository.findByApiKey(apiKey).ifPresentOrElse(
      apiKeyEntity -> {
        log.debug("API KEY found: {}", apiKeyEntity);
        SecurityContextHolder.getContext().setAuthentication(
          new UsernamePasswordAuthenticationToken(
            apiKeyEntity.getClient(),
            null,
            List.of(new SimpleGrantedAuthority(apiKeyEntity.getStatus().name()))
          ));
      },
      () -> {
        log.debug("API KEY not found");
        throw new BadCredentialsException("Invalid API KEY");
      }
    );

    filterChain.doFilter(request, response);
  }
}
