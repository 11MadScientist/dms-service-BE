/*
 * Copyright (c) 2XXX 11MadScientist
 * All Rights Reserved.
 * 
 * Project Name: dms-service-repositories
 */
package dev.madscientist.proj.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

import dev.madscientist.proj.users.service.UserDetailServiceImpl;
import dev.madscientist.proj.utils.Jwks;

/**
 * <b>File Created</b>: Feb 24, 2024
 * 
 * @author 11MadScientist
 */

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private RSAKey rsaKey;

  @Bean
  SecurityFilterChain configure(HttpSecurity http) throws Exception {
    return http.csrf(csrf -> csrf.disable()).authorizeHttpRequests(auth -> {
      auth.requestMatchers("/auth", "/api/login", "/api/register", "/api/logout", "/api/user")
          .permitAll();
      auth.anyRequest().authenticated();
      //      auth.requestMatchers("/api/gdrive/getRedirectURL").hasAnyRole(
      //          Roles.SUPER_ADMINISTRATOR.toString(),
      //          Roles.ADMINISTRATOR.toString(), Roles.GENERAL_USER.toString());
    })
        .oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults()))
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .build();
  }

  @Bean
  WebMvcConfigurer corsConfigurer() {
    return new WebMvcConfigurer() {
      @Override
      public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("*") // Allow requests from this origin
            .allowedMethods("*") // Allowed HTTP methods
            .allowCredentials(false); // Allow sending cookies
      }
    };
  }


  /**
   * proper way to get an authentication manager instance
   * @param user
   * @return
   * @throws Exception
   */
  @Bean
  AuthenticationManager authenticationManager(UserDetailServiceImpl user)
      throws Exception {
    var authProvider = new DaoAuthenticationProvider(encoder());
    authProvider.setUserDetailsService(user);
    return new ProviderManager(authProvider);
  }

  @Bean
  PasswordEncoder encoder() {
    return new BCryptPasswordEncoder();
  }

  //#### JWT beans

  @Bean
  JWKSource<SecurityContext> jwkSource() {
    rsaKey = Jwks.generateRsa();
    JWKSet jwkSet = new JWKSet(rsaKey);
    return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
  }

  @Bean
  JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwks) {
    return new NimbusJwtEncoder(jwks);
  }

  @Bean
  JwtDecoder jwtDecoder() throws JOSEException {
    return NimbusJwtDecoder.withPublicKey(rsaKey.toRSAPublicKey()).build();
  }

}
