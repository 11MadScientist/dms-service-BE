/*
 * Copyright (c) 2XXX 11MadScientist
 * All Rights Reserved.
 * 
 * Project Name: dms-service-repositories
 */
package dev.madscientist.proj.users.controllers;

import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.madscientist.proj.users.dtos.LoginCredentialsDTO;
import dev.madscientist.proj.users.dtos.UserProfileDTO;
import dev.madscientist.proj.users.entities.UserProfileEntity;
import dev.madscientist.proj.users.service.UserProfileService;


/**
 * Controller for USER_PROFILE
 * <b>File Created</b>: Feb 24, 2024
 * 
 * @author 11MadScientist
 */

@RestController
@RequestMapping("/api/")
public class UserProfileController {
  private final UserProfileService userSvc;

  public UserProfileController(UserProfileService userSvc) {
    this.userSvc = userSvc;
  }

  @GetMapping("/user")
  public Principal user(Principal user) {
    return user;
  }


  @PostMapping("/login")
  public UserProfileDTO login(@RequestBody LoginCredentialsDTO loginCredentials)
      throws Exception {
    return userSvc.authenticateUser(loginCredentials);
  }

  @PostMapping("/logout")
  public ResponseEntity<?> logout() {
    return ResponseEntity.ok("Logout Successfully");
  }

  @PostMapping("/register")
  public String registerUser(@RequestBody UserProfileEntity user) throws Exception {
    userSvc.saveUserProfile(user);
    return "hello";
  }

}
