/*
 * Copyright (c) 2XXX 11MadScientist
 * All Rights Reserved.
 * 
 * Project Name: dms-service-repositories
 */
package dev.madscientist.proj.users.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import dev.madscientist.proj.security.services.JWTTokenService;
import dev.madscientist.proj.users.dtos.LoginCredentialsDTO;
import dev.madscientist.proj.users.dtos.UserProfileDTO;
import dev.madscientist.proj.users.entities.UserProfileEntity;
import dev.madscientist.proj.users.repositories.UserProfileRepository;

/**
 * Service file of USER_PROFILE
 * <b>File Created</b>: Feb 24, 2024
 * 
 * @author 11MadScientist
 */

@Service
public class UserProfileService {

  private UserProfileRepository userProfileRepo;
  private PasswordEncoder passEncoder;
  private AuthenticationManager authManager;
  private ModelMapper modelMapper;
  private JWTTokenService tokenService;

  @Value("${default.disable.value}")
  private String defaultDisabledValue;

  public UserProfileService(UserProfileRepository userProfileRepo, PasswordEncoder passEncoder,
      AuthenticationManager authManager,
      ModelMapper modelMapper, JWTTokenService tokenService) {
    this.userProfileRepo = userProfileRepo;
    this.passEncoder = passEncoder;
    this.authManager = authManager;
    this.modelMapper = modelMapper;
    this.tokenService = tokenService;
  }

  /**
   * this method makes sure that user cannot update data that should not be updated
   * i.e username, password, etc.
   * this method encodes the password to bcrypt before storing it to the database.
   * @param id
   * @param updatedUserProfile
   * @return
   * @throws Exception 
   */

  /**
   * saves the user to database
   * @param updatedUserProfile
   * @return
   * @throws Exception
   */
  public UserProfileEntity saveUserProfile(UserProfileEntity updatedUserProfile)
      throws Exception {

    UserProfileEntity existingUserProfile = userProfileRepo.findByid(updatedUserProfile.getId());

    if (existingUserProfile != null) {
      existingUserProfile.setFirstName(updatedUserProfile.getFirstName());
      existingUserProfile.setLastName(updatedUserProfile.getLastName());
      existingUserProfile.setEmailAddress(updatedUserProfile.getEmailAddress());
      existingUserProfile.setRole(updatedUserProfile.getRole());

      return userProfileRepo.save(existingUserProfile);
    } else {
      // check if username is unique
      if (null != userProfileRepo.findByUsername(updatedUserProfile.getUsername())) {
        throw new Exception("Username already taken");
      }

      updatedUserProfile.setPassword(passEncoder.encode(updatedUserProfile.getPassword()));
      updatedUserProfile.setDisabled(defaultDisabledValue);
      return userProfileRepo.save(updatedUserProfile);
    }
  }

  /**
   * authenticates the user
   * @param loginCredentials
   * @return
   * @throws Exception
   */
  public UserProfileDTO authenticateUser(LoginCredentialsDTO loginCredentials) throws Exception {

    Authentication auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(
        loginCredentials.getUsername(), loginCredentials.getPassword()));
    
    UserProfileDTO userProfile = modelMapper.map(userProfileRepo.findByUsername(loginCredentials.getUsername()), UserProfileDTO.class);
    userProfile.setToken(tokenService.generateToken(auth));
    return userProfile;

  }

  /**
   * returnes the user using username
   * @param userName
   * @return
   */
  public UserProfileEntity findByUsername(String userName) {
    UserProfileEntity user = userProfileRepo.findByUsername(userName);
    if (user == null) {
      throw new UsernameNotFoundException("Invalid Username");
    }
    return user;
  }
}
