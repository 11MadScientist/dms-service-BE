/*
 * Copyright (c) 2XXX 11MadScientist
 * All Rights Reserved.
 * 
 * Project Name: dms-service-repositories
 */
package dev.madscientist.proj.entities;

import dev.madscientist.proj.serializables.DMSAdapterInfoId;
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
@IdClass(DMSAdapterInfoId.class)
@Table(name = "DMS_ADAPTER_INFO")
public class DMSAdapterInfoEntity {

  @Id
  @Column(name = "ADAPTER_NAME")
  private String adapterName;

  @Id
  @Column(name = "USER_ID")
  private long userId;

  @Column(name = "EXTENDED_AUTH_INFO")
  private String extendedAuthInfo;

  @NotNull
  @Column(name = "AUTH_INFO")
  private String authInfo;

  @Column(name = "MISC_INFO")
  private String miscInfo;

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

  /**
   * @return the extendedAuthInfo
   */
  public String getExtendedAuthInfo() {
    return extendedAuthInfo;
  }

  /**
   * @param extendedAuthInfo the extendedAuthInfo to set
   */
  public void setExtendedAuthInfo(String extendedAuthInfo) {
    this.extendedAuthInfo = extendedAuthInfo;
  }

  /**
   * @return the authInfo
   */
  public String getAuthInfo() {
    return authInfo;
  }

  /**
   * @param authInfo the authInfo to set
   */
  public void setAuthInfo(String authInfo) {
    this.authInfo = authInfo;
  }

  /**
   * @return the miscInfo
   */
  public String getMiscInfo() {
    return miscInfo;
  }

  /**
   * @param miscInfo the miscInfo to set
   */
  public void setMiscInfo(String miscInfo) {
    this.miscInfo = miscInfo;
  }

}
