/*
 * Copyright (c) 2XXX 11MadScientist
 * All Rights Reserved.
 * 
 * Project Name: dms-service-repositories
 */
package dev.madscientist.proj.dms.dtos;

import java.util.Date;

/**
 * <b>File Created</b>: Mar 8, 2024
 * 
 * @author 11MadScientist
 */
public class FileInfoDTO {
  private String id;
  private String name;
  private String mimeType;
  private String fileType;
  private String parentId;
  private Long size;
  private Date createDt;
  private Date modifiedDt;
  
  
  
  /**
   * Constructor
   *
   */
  public FileInfoDTO() {
  }
  
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
   * @return the fileType
   */
  public String getFileType() {
    return fileType;
  }
  /**
   * @param fileType the fileType to set
   */
  public void setFileType(String fileType) {
    this.fileType = fileType;
  }
  
  /**
   * @return the parentId
   */
  public String getParentId() {
    return parentId;
  }

  /**
   * @param parentId the parentId to set
   */
  public void setParentId(String parentId) {
    this.parentId = parentId;
  }

  /**
   * @return the size
   */
  public Long getSize() {
    return size;
  }
  /**
   * @param size the size to set
   */
  public void setSize(Long size) {
    this.size = size;
  }
  /**
   * @return the createDt
   */
  public Date getCreateDt() {
    return createDt;
  }
  /**
   * @param createDt the createDt to set
   */
  public void setCreateDt(Date createDt) {
    this.createDt = createDt;
  }
  /**
   * @return the modifiedDt
   */
  public Date getModifiedDt() {
    return modifiedDt;
  }
  /**
   * @param modifiedDt the modifiedDt to set
   */
  public void setModifiedDt(Date modifiedDt) {
    this.modifiedDt = modifiedDt;
  }

  @Override
  public String toString() {
    return "FileInfoDTO [id=" + id + ", name=" + name + ", mimeType=" + mimeType + ", fileType="
        + fileType + ", parentId=" + parentId + ", size=" + size + ", createDt=" + createDt
        + ", modifiedDt=" + modifiedDt + "]";
  }
  
  
  
}
