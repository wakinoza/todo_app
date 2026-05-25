package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(
        auth -> auth.requestMatchers("/css/**", "/js/**", "/images/**", "/login").permitAll()
            .anyRequest().authenticated())

        .formLogin(form -> form.loginPage("/login").defaultSuccessUrl("/todo/loginResult", true)
            .loginProcessingUrl("/login").permitAll())

        .logout(logout -> logout.logoutUrl("/logout").logoutSuccessUrl("/login?logout")
            .invalidateHttpSession(true).deleteCookies("JSESSIONID"));

    return http.build();
  }
}
