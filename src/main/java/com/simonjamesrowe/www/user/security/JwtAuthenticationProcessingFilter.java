package com.simonjamesrowe.www.user.security;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {

  public static final String HEADER_AUTHORIZATION = "Authorization";
  private JwtTokenManager jwtTokenManager;

  public JwtAuthenticationProcessingFilter(JwtTokenManager jwtTokenManager) {
    super("/**");
    this.jwtTokenManager = jwtTokenManager;
  }

  @Override
  protected boolean requiresAuthentication(
      HttpServletRequest request, HttpServletResponse response) {
    return StringUtils.isNotBlank(request.getHeader(HEADER_AUTHORIZATION));
  }

  @Override
  public Authentication attemptAuthentication(
      HttpServletRequest request, HttpServletResponse response)
      throws AuthenticationException, IOException, ServletException {
    String token = request.getHeader(HEADER_AUTHORIZATION).substring(7);
    try {
      return jwtTokenManager.decodeToken(token);
    } catch (Exception e) {
      return null;
    }
  }

  @Override
  protected void successfulAuthentication(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain chain,
      Authentication authResult)
      throws IOException, ServletException {

    if (logger.isDebugEnabled()) {
      logger.debug(
          "Authentication success. Updating SecurityContextHolder to contain: " + authResult);
    }

    SecurityContextHolder.getContext().setAuthentication(authResult);

    // Fire event
    if (this.eventPublisher != null) {
      eventPublisher.publishEvent(
          new InteractiveAuthenticationSuccessEvent(authResult, this.getClass()));
    }

    chain.doFilter(request, response);
  }
}
