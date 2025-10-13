package dev.todoapp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

@Configuration
public class LogoutSuccessHandlerConfig {

  @Bean
  @ConditionalOnProperty(prefix = "custom", name = "use-cognito-as-identity-provider", havingValue = "true")
  public LogoutSuccessHandler cognitoOidcLogoutSuccessHandler(
    @Value("${COGNITO_CLIENT_ID}") String clientId,
    @Value("${COGNITO_LOGOUT_URL}") String userPoolLogoutUrl) {
    return new CognitoOidcLogoutSuccessHandler(userPoolLogoutUrl, clientId);
  }

  @Bean
  @ConditionalOnProperty(prefix = "custom", name = "use-cognito-as-identity-provider", havingValue = "false")
  public LogoutSuccessHandler simpleLogoutSuccessHandler() {
    SimpleUrlLogoutSuccessHandler handler = new SimpleUrlLogoutSuccessHandler();
    handler.setDefaultTargetUrl("/");
    return handler;
  }
}
