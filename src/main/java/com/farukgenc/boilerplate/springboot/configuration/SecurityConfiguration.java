package com.farukgenc.boilerplate.springboot.configuration;

import com.farukgenc.boilerplate.springboot.security.jwt.JwtAuthenticationEntryPoint;
import com.farukgenc.boilerplate.springboot.security.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.*;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Created on Ağustos, 2020
 *
 * @author Faruk
 */
@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration {

  private final JwtAuthenticationFilter jwtAuthenticationFilter;

  private final JwtAuthenticationEntryPoint unauthorizedHandler;

  @Bean
  public AuthenticationManager authenticationManager(final AuthenticationConfiguration authenticationConfiguration)
      throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    return http
        .csrf(CsrfConfigurer::disable)
        .cors(CorsConfigurer::disable)
        // .addFilterBefore(jwtAuthenticationFilter,
        // UsernamePasswordAuthenticationFilter.class)
        .authorizeHttpRequests(request -> request.requestMatchers("/register",
            "/login",
            "/hello",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/actuator/**")
            .permitAll()
            .anyRequest()
            .permitAll())
        .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .exceptionHandling(handler -> handler.authenticationEntryPoint(unauthorizedHandler))
        .build();

  }

}
