/*
 * Copyright (c) 2XXX 11MadScientist
 * All Rights Reserved.
 * 
 * Project Name: dms-service-repositories
 */
package dev.madscientist.proj.dms.dmsadapters;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.google.api.services.drive.model.File;

import dev.madscientist.proj.dms.dtos.DocInfoDTO;
import dev.madscientist.proj.dms.dtos.FileInfoDTO;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Interface to be implemented by DMS Adapters.
 * contains methods that should be implemented in a DMS Adapter class.
 * <b>File Created</b>: Feb 21, 2024
 * 
 * @author 11MadScientist
 */
public interface DMSAdapter {
  /**
   * Returns the Files (folders and documents) from DMS
   * @param userId
   * @param folderId
   * @return
   * @throws Exception
   */
  public List<FileInfoDTO> getFileList(Long userId, String folderId) throws Exception;
  
  /**
   * Returns the information of the file
   * @param userId
   * @param folderId
   * @return
   * @throws Exception
   */
  public FileInfoDTO getFileInfo(Long userId, String folderId) throws Exception;
  
  /**
   * Downloads the file from the DMS
   * @param userId
   * @param response
   * @param googleDoc
   * @throws Exception
   */
  public void downloadFile(long userId, HttpServletResponse response, FileInfoDTO googleDoc)
      throws Exception;

  /**
   * Uploads the file into the DMS
   * @param userId
   * @param files
   * @param parentId
   * @throws Exception
   */
  public void uploadFile(long userId, MultipartFile[] files, String parentId) throws Exception;
  
  /**
   * Creates folder into the DMS
   * @param userId
   * @param parentId
   * @param folderName
   * @throws Exception
   */
  public void createFolder(long userId, String parentId, String folderName) throws Exception;

  /**
   * gets the names of folder existing in the parent folder (owner of the id)
   * @param userId
   * @param parentId
   * @return
   * @throws Exception
   */
  public List<String> getFolderNames(long userId, String parentId) throws Exception;

  

  /**
   * Provides the redirect url for the user to use 
   * to give the app the authorization to access the DMS
   * @return
   */
  public String getRedirectURL();

  /**
   * Saves the credentials of the user after authorizing the app to access the DMS.
   * The saved credentials will be used by the app to access the DMS.
   * @param id
   * @param code
   * @return
   * @throws Exception
   */
  public String saveAuthorizationCode(Long id, String code) throws Exception;

  
  
  
}
