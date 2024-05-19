/*
 * Copyright (c) 2XXX 11MadScientist
 * All Rights Reserved.
 * 
 * Project Name: dms-service-repositories
 */
package dev.madscientist.proj.serializables;

import java.io.Serializable;
import java.util.Objects;

/**
 * The @IdClass of DMSAdapterInfoEntity
 * <b>File Created</b>: Feb 23, 2024
 * 
 * @author 11MadScientist
 */
public class DMSAdapterInfoId implements Serializable {

  private String adapterName;

  private long userId;

  /**
   * empty
   * Constructor
   *
   */
  public DMSAdapterInfoId() {

  }

  /**
   * Constructor
   *
   * @param adapterName
   * @param userId
   */
  public DMSAdapterInfoId(String adapterName, long userId) {
    super();
    this.adapterName = adapterName;
    this.userId = userId;
  }

  @Override
  public int hashCode() {
    return Objects.hash(adapterName, userId);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    DMSAdapterInfoId other = (DMSAdapterInfoId) obj;
    return Objects.equals(adapterName, other.adapterName) && userId == other.userId;
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
   * @return the userId
   */
  public long getUserId() {
    return userId;
  }

  /**
   * @param userId the userId to set
   */
  public void setUserId(long userId) {
    this.userId = userId;
  }

}
