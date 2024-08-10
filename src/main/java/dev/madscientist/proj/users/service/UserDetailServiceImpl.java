/*
 * Copyright (c) 2XXX 11MadScientist
 * All Rights Reserved.
 * 
 * Project Name: dms-service-repositories
 */
package dev.madscientist.proj.users.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import dev.madscientist.proj.users.entities.UserProfileEntity;
import dev.madscientist.proj.users.repositories.UserProfileRepository;

/**
 * <b>File Created</b>: Feb 25, 2024
 * 
 * @author 11MadScientist
 */
@Service
public class UserDetailServiceImpl implements UserDetailsService {

  private UserProfileRepository userRepo;

  public UserDetailServiceImpl(UserProfileRepository userRepo) {
    this.userRepo = userRepo;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    UserProfileEntity user = userRepo.findByUsername(username);
    if (user == null) {
      throw new UsernameNotFoundException("User not found with username: " + username);
    }
    return org.springframework.security.core.userdetails.User.withUsername(username)
        .password(user.getPassword()).roles(user.getRole()).build();
  }

}
