package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import com.example.demo.entity.User;
import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableWebSecurity
@Slf4j
public class SecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(
        auth -> auth.requestMatchers("/css/**", "/js/**", "/images/**", "/login").permitAll()
            .anyRequest().authenticated())

        .formLogin(form -> form.loginPage("/login").defaultSuccessUrl("/todo/list", true)
            .loginProcessingUrl("/login").permitAll())

        .logout(logout -> logout.logoutUrl("/logout").invalidateHttpSession(true)
            .deleteCookies("JSESSIONID")
            .logoutSuccessHandler((request, response, authentication) -> {

              if (authentication != null && authentication.getPrincipal() instanceof User) {
                User loginUser = (User) authentication.getPrincipal();
                log.info("ユーザーがログアウトしました。ユーザー名: [{}] (ID: {})", loginUser.getName(),
                    loginUser.getId());
              } else {
                log.info("セッションが存在しない、またはゲストがログアウトしました。");
              }

              response.sendRedirect("/login?logout");
            }));

    return http.build();
  }
}
