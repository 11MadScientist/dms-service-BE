/*
 * Copyright (c) 2XXX 11MadScientist
 * All Rights Reserved.
 * 
 * Project Name: dms-service-repositories
 */
package dev.madscientist.proj.dms.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.madscientist.proj.dms.entities.DMSAdapterConfigEntity;
import dev.madscientist.proj.dms.entities.DMSAdapterInfoEntity;
import dev.madscientist.proj.dms.serializables.DMSAdapterConfigId;
import dev.madscientist.proj.dms.serializables.DMSAdapterInfoId;

/**
 * Repository for DMS_ADAPTER_INFO table
 * <b>File Created</b>: Feb 23, 2024
 * 
 * @author 11MadScientist
 */

@Repository
public interface DMSAdapterConfigRepository
    extends JpaRepository<DMSAdapterConfigEntity, DMSAdapterConfigId> {
  Optional<DMSAdapterConfigEntity> findById(DMSAdapterConfigId id);
}
