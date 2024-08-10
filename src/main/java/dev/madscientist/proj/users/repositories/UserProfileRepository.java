/*
 * Copyright (c) 2XXX 11MadScientist
 * All Rights Reserved.
 * 
 * Project Name: dms-service-repositories
 */
package dev.madscientist.proj.users.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.madscientist.proj.users.entities.UserProfileEntity;

/**
 * repository file of USER_PROFILE table
 * <b>File Created</b>: Feb 24, 2024
 * 
 * @author 11MadScientist
 */

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfileEntity, Long> {
  UserProfileEntity findByid(long id);

  UserProfileEntity findByUsername(String username);
}
