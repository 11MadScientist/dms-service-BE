/*
 * Copyright (c) 2XXX 11MadScientist
 * All Rights Reserved.
 * 
 * Project Name: dms-service-be
 */
package dev.madscientist.proj.dms.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import dev.madscientist.proj.dms.entities.DMSAdapterConfigEntity;
import dev.madscientist.proj.dms.repositories.DMSAdapterConfigRepository;
import dev.madscientist.proj.dms.serializables.DMSAdapterConfigId;

/**
 * <b>File Created</b>: May 19, 2024
 * 
 * @author 11MadScientist
 */
@Service
public class DMSAdapterConfigService {
  private final DMSAdapterConfigRepository adapterConfigRepo;
  
  /**
   * Constructor
   */
  public DMSAdapterConfigService(DMSAdapterConfigRepository adapterConfigRepo) {
    this.adapterConfigRepo = adapterConfigRepo;
  }
  
  /**
   * 
   * @param configId
   * @return VALUE of the config
   */
  public String getConfigValue(DMSAdapterConfigId configId) {
    Optional<DMSAdapterConfigEntity> adapterConfig = adapterConfigRepo.findById(configId);
    return adapterConfig.get().getValue();
  }
}
