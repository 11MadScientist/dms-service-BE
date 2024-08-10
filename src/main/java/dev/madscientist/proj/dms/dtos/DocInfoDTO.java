/*
 * Copyright (c) 2XXX 11MadScientist
 * All Rights Reserved.
 * 
 * Project Name: dms-service-repositories
 */
package dev.madscientist.proj.dms.dtos;

import com.google.api.client.util.DateTime;

/**
 * <b>File Created</b>: Feb 28, 2024
 * 
 * @author 11MadScientist
 */
public class DocInfoDTO {

  public DocInfoDTO() {

  }

  private String id;
  private String name;
  private String mimeType;
  private DateTime modifiedTime;

  /**
   * @return the id
   */
  public String getId() {
    return id;
  }

  /**
   * @param id the id to set
   */
  public void setId(String id) {
    this.id = id;
  }

  /**
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * @param name the name to set
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * @return the mimeType
   */
  public String getMimeType() {
    return mimeType;
  }

  /**
   * @param mimeType the mimeType to set
   */
  public void setMimeType(String mimeType) {
    this.mimeType = mimeType;
  }

  /**
   * @return the modifiedTime
   */
  public DateTime getModifiedTime() {
    return modifiedTime;
  }

  /**
   * @param modifiedTime the modifiedTime to set
   */
  public void setModifiedTime(DateTime modifiedTime) {
    this.modifiedTime = modifiedTime;
  }

}
