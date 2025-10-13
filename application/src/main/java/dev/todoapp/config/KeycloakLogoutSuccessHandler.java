package dev.todoapp.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

public class KeycloakLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {

  @Override
  protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response,
                                      Authentication authentication) {
    return "http://localhost:8888/auth/realms/stratospheric/protocol/openid-connect/logout?redirect_uri=http://localhost:8080/";
  }
}
