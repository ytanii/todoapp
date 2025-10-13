package dev.todoapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class WebSecurityConfig {

  private final LogoutSuccessHandler logoutSuccessHandler;

  public WebSecurityConfig(LogoutSuccessHandler logoutSuccessHandler) {
    this.logoutSuccessHandler = logoutSuccessHandler;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
    httpSecurity
      .csrf(csrf -> csrf.ignoringRequestMatchers(
        "/stratospheric-todo-updates/**",
        "/websocket/**"
      ))
      .oauth2Login(withDefaults())
      .authorizeHttpRequests(httpRequests -> httpRequests
        .requestMatchers("/", "/register").permitAll()
        .anyRequest().authenticated())
      .logout(logout -> logout.logoutSuccessHandler(logoutSuccessHandler));

    return httpSecurity.build();
  }
}
