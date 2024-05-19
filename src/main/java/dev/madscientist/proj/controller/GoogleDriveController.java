/*
 * Copyright (c) 2XXX 11MadScientist
 * All Rights Reserved.
 * 
 * Project Name: dms-service-repositories
 */
package dev.madscientist.proj.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import dev.madscientist.proj.dmsadapters.GoogleDriveAdapter;
import dev.madscientist.proj.dtos.FileInfoDTO;
import dev.madscientist.proj.services.DMSAdapterService;
import jakarta.servlet.http.HttpServletResponse;

/**
 * <b>File Created</b>: Feb 17, 2024
 * 
 * @author 11MadScientist
 */

@RestController
@CrossOrigin("http://localhost:9000")
@RequestMapping("/api/gdrive")
public class GoogleDriveController {

  private final DMSAdapterService adapterSvc;

  public GoogleDriveController(DMSAdapterService adapterSvc) {
    this.adapterSvc = adapterSvc;
  }

  /**
   * returns the list of files inside the folder
   * @param folderId the Id of the folder the user is trying to open
   * @return
   * @throws Exception
   */
  @GetMapping("/getFileList")
  public List<FileInfoDTO> getFileList(@RequestParam(required=false, name="folderId") String folderId) throws Exception {
    return adapterSvc.getFileList(GoogleDriveAdapter.GDRIVE_CODE, folderId);
  }
  
  /**
   * returns the information of the file
   * @param fileId the id of the file the user is trying to get the information of.
   * @return
   * @throws Exception
   */
  @GetMapping("/getFileInfo")
  public FileInfoDTO getFileInfo(@RequestParam(name="fileId") String fileId) throws Exception {
    return adapterSvc.getFileInfo(GoogleDriveAdapter.GDRIVE_CODE, fileId);
  }

  /**
   * downloads the file from gdrive
   * @param response
   * @param fileInfo
   * @throws Exception
   */
  @PostMapping("/downloadFile")
  public void downloadFile(HttpServletResponse response, @RequestBody FileInfoDTO fileInfo) throws Exception {
    adapterSvc.downloadFile(GoogleDriveAdapter.GDRIVE_CODE, response, fileInfo);
  }
  
  /**
   * uploads file into the gdrive
   * @param files
   * @param parentId
   * @throws Exception
   */
  @PostMapping(value="/uploadFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public void uploadFile(@RequestParam("files") MultipartFile[] files, @RequestParam("parentId") String parentId) throws Exception {
   adapterSvc.uploadFile(GoogleDriveAdapter.GDRIVE_CODE, files, parentId);
  }
  
  /**
   * gets the folder names that is present in the parent folder
   * @param folderId
   * @return 
   * @throws Exception 
   */
  @GetMapping("/getFolderNames")
  public List<String> getFolderNames(@RequestParam("folderId") String folderId) throws Exception {
    return adapterSvc.getFolderNames(GoogleDriveAdapter.GDRIVE_CODE, folderId);
  }
  
  /**
   * creates the folder into gdrive
   * @param folderId
   * @param folderName
   * @throws Exception 
   */
  @GetMapping("/createFolder")
  public void createFolder(@RequestParam("folderId") String folderId, @RequestParam("folderName") String folderName) throws Exception {
     adapterSvc.createFolder(GoogleDriveAdapter.GDRIVE_CODE, folderId, folderName);
  }
  
  /**
   * provides the redirect String URL
   * @param response
   * @param principal
   * @return
   * @throws IOException
   */
  @GetMapping("/getRedirectURL")
  public String getRedirectURL(HttpServletResponse response, Principal principal)
      throws IOException {
    return adapterSvc.getRedirectURL(GoogleDriveAdapter.GDRIVE_CODE);
  }
}
