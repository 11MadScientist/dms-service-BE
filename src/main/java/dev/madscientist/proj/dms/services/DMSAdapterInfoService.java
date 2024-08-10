/*
 * Copyright (c) 2XXX 11MadScientist
 * All Rights Reserved.
 * 
 * Project Name: dms-service-be
 */
package dev.madscientist.proj.dms.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.madscientist.proj.dms.entities.DMSAdapterInfoEntity;
import dev.madscientist.proj.dms.repositories.DMSAdapterInfoRepository;
import dev.madscientist.proj.dms.serializables.DMSAdapterInfoId;

/**
 * <b>File Created</b>: May 19, 2024
 * 
 * @author 11MadScientist
 */
@Service
public class DMSAdapterInfoService {
  private final DMSAdapterInfoRepository adapterInfoRepo;
  
  /**
   * Constructor
   */
  public DMSAdapterInfoService(DMSAdapterInfoRepository adapterInfoRepo) {
    this.adapterInfoRepo = adapterInfoRepo;
  }
  
  /**
   * 
   * @param id
   * @return DMSAdapterInfoEntity
   */
  public DMSAdapterInfoEntity getAuthInfo(DMSAdapterInfoId id) {
    Optional<DMSAdapterInfoEntity> adapterInfo = adapterInfoRepo.findById(id);
    return adapterInfo.get();
  }
  
  public void save(DMSAdapterInfoEntity adapterInfo) {
    adapterInfoRepo.save(adapterInfo);
  }
}
