/*
 * Copyright (c) 2XXX 11MadScientist
 * All Rights Reserved.
 * 
 * Project Name: dms-service-repositories
 */
package dev.madscientist.proj.dms.services;

import java.io.IOException;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.api.services.drive.model.File;

import dev.madscientist.proj.dms.dmsadapters.AdapterManager;
import dev.madscientist.proj.dms.dtos.DocInfoDTO;
import dev.madscientist.proj.dms.dtos.FileInfoDTO;
import dev.madscientist.proj.dms.repositories.DMSAdapterInfoRepository;
import dev.madscientist.proj.users.entities.UserProfileEntity;
import dev.madscientist.proj.users.service.UserProfileService;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Service file of DMS_ADAPTER_INFO
 * <b>File Created</b>: Feb 17, 2024
 * 
 * @author 11MadScientist
 */

@Service
public class DMSAdapterService {

  private final AdapterManager adapterManager;
  private final UserProfileService userService;

  public DMSAdapterService(AdapterManager adapterManager,
      DMSAdapterInfoRepository adapterRepository, UserProfileService userService) {
    this.adapterManager = adapterManager;
    this.userService = userService;
  }

  /**
   * returns list of files present in the gdrive folder
   * @param adapterName
   * @return
   * @throws Exception
   */
  public List<FileInfoDTO> getFileList(String adapterName, String folderId) throws Exception {
    return adapterManager.getAdapter(adapterName).getFileList(getUserId(), folderId);
  }
  
  /**
   * returns the information of the file
   * @param adapterName
   * @param fileId
   * @return
   * @throws Exception
   */
  public FileInfoDTO getFileInfo(String adapterName, String fileId) throws Exception {
    return adapterManager.getAdapter(adapterName).getFileInfo(getUserId(), fileId);
  }
  
  /**
   * downloads the file
   * @param adapterName
   * @param response
   * @param fileInfo
   * @throws Exception
   */
  public void downloadFile(String adapterName, HttpServletResponse response, FileInfoDTO fileInfo)
      throws Exception {
    adapterManager.getAdapter(adapterName).downloadFile(getUserId(), response, fileInfo);

  }
  /**
   * uploads the file
   * @param adapterName
   * @param files
   * @param parentId
   * @throws Exception
   */
  public void uploadFile(String adapterName, MultipartFile[] files, String parentId) throws Exception{
    adapterManager.getAdapter(adapterName).uploadFile(getUserId(), files, parentId);
  }
  
  /**
   * Creates Folder into the DMS
   * @param adapterName
   * @param parentId
   * @param folderName
   * @throws Exception
   */
  public void createFolder(String adapterName, String parentId, String folderName) throws Exception {
    adapterManager.getAdapter(adapterName).createFolder(getUserId(), parentId, folderName);
  }
  
  /**
   * gets the names of folder existing from the parent folder
   * @param adapterName
   * @param parentId
   * @return List of Folder Names
   * @throws Exception 
   */
  public List<String> getFolderNames(String adapterName, String parentId) throws Exception {
    return adapterManager.getAdapter(adapterName).getFolderNames(getUserId(), parentId);
  }
  
  /**
   * gets the user id from Authentication
   * @return
   */
  private Long getUserId() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return userService.findByUsername(authentication.getName()).getId();
  }
  
  /**
   * function that gets the redirect url depending on the passed name
   * GD = Google Drive
   * @param adapterName
   * @return
   * @throws IOException
   */
  public String getRedirectURL(String adapterName) throws IOException {
    return adapterManager.getAdapter(adapterName).getRedirectURL();
  }

  /**
   * gets the access token, refresh token, etc and save it in the db
   * @param id
   * @param code
   * @return
   * @throws Exception
   */
  public String saveAuthorizationCode(String adapterName, String code)
      throws Exception {
    return adapterManager.getAdapter(adapterName).saveAuthorizationCode(getUserId(), code);
  }
}
