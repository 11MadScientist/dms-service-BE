/*
 * Copyright (c) 2XXX 11MadScientist
 * All Rights Reserved.
 * 
 * Project Name: dms-service-repositories
 */
package dev.madscientist.proj.dms.entities;

import dev.madscientist.proj.dms.serializables.DMSAdapterConfigId;
import dev.madscientist.proj.dms.serializables.DMSAdapterInfoId;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

/**
 * Entity for DMS_ADAPTER_INFO table which holds the
 * access token, refresh token, etc of the user using 
 * the certain adapter.
 * <b>File Created</b>: Feb 23, 2024
 * 
 * @author 11MadScientist
 */

@Entity
@IdClass(DMSAdapterConfigId.class)
@Table(name = "DMS_ADAPTER_CONFIG")
public class DMSAdapterConfigEntity {

  @Id
  @Column(name = "ADAPTER_NAME")
  private String adapterName;

  @Id
  @Column(name = "CONFIG_NAME")
  private String configName;

  @Column(name = "VALUE")
  private String value;

  /**
   * @return the adapterName
   */
  public String getAdapterName() {
    return adapterName;
  }

  /**
   * @param adapterName the adapterName to set
   */
  public void setAdapterName(String adapterName) {
    this.adapterName = adapterName;
  }

  /**
   * @return the configName
   */
  public String getConfigName() {
    return configName;
  }

  /**
   * @param configName the configName to set
   */
  public void setConfigName(String configName) {
    this.configName = configName;
  }

  /**
   * @return the value
   */
  public String getValue() {
    return value;
  }

  /**
   * @param value the value to set
   */
  public void setValue(String value) {
    this.value = value;
  }
}
