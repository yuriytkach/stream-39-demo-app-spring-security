package com.yuriytkach.demo.demospringsecurity.jwt;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtService jwtService;

  @Override
  protected void doFilterInternal(
    final HttpServletRequest request,
    final HttpServletResponse response,
    final FilterChain filterChain
  )
    throws ServletException, IOException {

    if (SecurityContextHolder.getContext().getAuthentication() != null) {
      log.debug("SecurityContextHolder already contains authentication: '{}'",
        SecurityContextHolder.getContext().getAuthentication());
      filterChain.doFilter(request, response);
      return;
    }

    final String authHeader = request.getHeader("Authorization");
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      filterChain.doFilter(request, response);
      return;
    }

    final String jwtToken = authHeader.substring(7);

    jwtService.parseToken(jwtToken).ifPresentOrElse(
      parsedJwt -> {
        log.debug("Parsed JWT token for user: {}", parsedJwt.username());

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
          parsedJwt.username(),
          null,
          List.of(new SimpleGrantedAuthority(parsedJwt.role()))
        );
        authToken.setDetails(
          new WebAuthenticationDetailsSource().buildDetails(request)
        );
        SecurityContextHolder.getContext().setAuthentication(authToken);
      },
      () -> {
        log.debug("API KEY not found");
        throw new BadCredentialsException("Invalid JWT Token");
      }
    );

    filterChain.doFilter(request, response);
  }
}
