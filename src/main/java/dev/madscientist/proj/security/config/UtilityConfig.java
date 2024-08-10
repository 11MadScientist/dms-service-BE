/*
 * Copyright (c) 2XXX 11MadScientist
 * All Rights Reserved.
 * 
 * Project Name: dms-service-repositories
 */
package dev.madscientist.proj.security.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Class where you can declare @Bean of multiple utility dependencies
 * <b>File Created</b>: Feb 25, 2024
 * 
 * @author 11MadScientist
 */

@Configuration
public class UtilityConfig {
  @Bean
  public ModelMapper modelMapper() {
    return new ModelMapper();
  }
}
