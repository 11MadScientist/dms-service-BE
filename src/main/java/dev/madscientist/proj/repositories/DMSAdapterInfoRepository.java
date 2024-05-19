/*
 * Copyright (c) 2XXX 11MadScientist
 * All Rights Reserved.
 * 
 * Project Name: dms-service-repositories
 */
package dev.madscientist.proj.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.madscientist.proj.entities.DMSAdapterInfoEntity;
import dev.madscientist.proj.serializables.DMSAdapterInfoId;

/**
 * Repository for DMS_ADAPTER_INFO table
 * <b>File Created</b>: Feb 23, 2024
 * 
 * @author 11MadScientist
 */

@Repository
public interface DMSAdapterInfoRepository
    extends JpaRepository<DMSAdapterInfoEntity, DMSAdapterInfoId> {
  Optional<DMSAdapterInfoEntity> findById(DMSAdapterInfoId id);
}
