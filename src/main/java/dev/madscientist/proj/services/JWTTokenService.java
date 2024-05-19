/*
 * Copyright (c) 2XXX 11MadScientist
 * All Rights Reserved.
 * 
 * Project Name: dms-service-repositories
 */
package dev.madscientist.proj.services;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

/**
 * JWT Token Service
 * <b>File Created</b>: Feb 26, 2024
 * 
 * @author 11MadScientist
 */

@Service
public class JWTTokenService {
  private final JwtEncoder encoder;

  public JWTTokenService(JwtEncoder encoder) {
    this.encoder = encoder;
  }

  public String generateToken(Authentication auth) {
    Instant now = Instant.now();
    
    String scope = auth.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.joining(" "));
    
    JwtClaimsSet claims = JwtClaimsSet.builder()
        .issuer("self")
        .issuedAt(now)
        .expiresAt(now.plus(1, ChronoUnit.HOURS))
        .subject(auth.getName())
        .claim("scope", scope)
        .build();

    return this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
  }
}
