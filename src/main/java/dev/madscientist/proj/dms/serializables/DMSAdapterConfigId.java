/*
 * Copyright (c) 2XXX 11MadScientist
 * All Rights Reserved.
 * 
 * Project Name: dms-service-be
 */
package dev.madscientist.proj.dms.serializables;

import java.io.Serializable;
import java.util.Objects;

/**
 * <b>File Created</b>: May 19, 2024
 * 
 * @author 11MadScientist
 */
public class DMSAdapterConfigId implements Serializable {
  
  private String adapterName;
  private String configName;
  
  /**
   * Empty Constructor
   */
  public DMSAdapterConfigId() {
    
  }
  
  /**
   * Constructor
   *
   * @param adapterName
   * @param userId
   */
  public DMSAdapterConfigId(String adapterName, String configName) {
    super();
    this.adapterName = adapterName;
    this.configName = configName;
  }

  @Override
  public int hashCode() {
    return Objects.hash(adapterName, configName);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    DMSAdapterConfigId other = (DMSAdapterConfigId) obj;
    return Objects.equals(adapterName, other.adapterName)
        && Objects.equals(configName, other.configName);
  }

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
}
