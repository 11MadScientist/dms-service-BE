/*
 * Copyright (c) 2XXX 11MadScientist
 * All Rights Reserved.
 * 
 * Project Name: dms-service-repositories
 */
package dev.madscientist.proj.dms.dmsadapters;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets.Details;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.DriveList;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.UserCredentials;

import dev.madscientist.proj.dms.dtos.FileInfoDTO;
import dev.madscientist.proj.dms.entities.DMSAdapterInfoEntity;
import dev.madscientist.proj.dms.repositories.DMSAdapterInfoRepository;
import dev.madscientist.proj.dms.serializables.DMSAdapterConfigId;
import dev.madscientist.proj.dms.serializables.DMSAdapterInfoId;
import dev.madscientist.proj.dms.services.DMSAdapterConfigService;
import dev.madscientist.proj.dms.services.DMSAdapterInfoService;
import io.micrometer.common.util.StringUtils;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletResponse;
import jodd.net.MimeTypes;

/**
 * DMS Adapter for Google Drive 
 * <b>File Created</b>: Feb 21, 2024
 * 
 * @author 11MadScientist
 */

@Component
public class GoogleDriveAdapter implements DMSAdapter {
  Logger LOG = LoggerFactory.getLogger(GoogleDriveAdapter.class);
  public static final String GDRIVE_CODE = "GD";
  private final String APPLICATION_NAME = "DMS SERVICE REPOSITORY";
  private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
  private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
  private static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE);
  
  // secrets
  private final String clientId;
  private final String projectId;
  private final String authUri;
  private final String tokenUri;
  private final String authProviderX509CertUrl;
  private final String clientSecret;
  private final String callbackUri;

  // JSON value string names
  private final String ACCESS_TOKEN = "access_token";
  private final String REFRESH_TOKEN = "refresh_token";
  private final String TOKEN_TYPE = "token_type";
  private final String EXPIRES_IN = "expires_in";
  private final String SCOPE = "scope";
  
  // folder mimetype
  private final String MIMETYPE_FOLDER = "application/vnd.google-apps.folder";
  // supported google drive mimetypes to be converted
  private final String GOOGLE_DOCS = "application/vnd.google-apps.document";
  private final String GOOGLE_SPREADSHEET = "application/vnd.google-apps.spreadsheet";
  private final String GOOGLE_PRESENTATION = "application/vnd.google-apps.presentation";
  private final String GOOGLE_DRAWING = "application/vnd.google-apps.drawing";
  
  
  // default mimetypes for type of documents
  private final String EXPORT_DOC_TYPE = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
  private final String EXPORT_SPREADSHEET_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
  private final String EXPORT_PRESENTATION_TYPE = "application/vnd.openxmlformats-officedocument.presentationml.presentation";
  private final String EXPORT_DRAWING_TYPE = "application/pdf";
  
  private final String FOLDER = "folder";
  // filetype of the google files after conversion
  private final String DOCUMENT = "docx";
  private final String EXCEL = "xlsx";
  private final String PDF = "pdf";
  private final String PPT = "pptx";

  // extension type of the documents
  private final Map<String, String> extensionType = new ConcurrentHashMap<String, String>();

  // map for mimetype and export mimetype
  private Map<String, String> downloadMimeMap = new ConcurrentHashMap<>();
  
  // map filetype and export mimetype
  private Map<String, String> downloadFileType = new ConcurrentHashMap<>();
  
  private Map<String, String> defaultFolders = new ConcurrentHashMap<>();

  private GoogleAuthorizationCodeFlow flow;
  private String redirectUrl;
  private final AdapterManager adapterManager;
  private final DMSAdapterInfoService adapterInfoService;
  private final DMSAdapterConfigService adapterConfigService;

  public GoogleDriveAdapter(AdapterManager adapterManager,
      DMSAdapterInfoService adapterInfoService,
      DMSAdapterConfigService adapterConfigService) {
    this.adapterInfoService = adapterInfoService;
    this.adapterConfigService = adapterConfigService;
    //adding the adapter to adapter manager
    this.adapterManager = adapterManager;
    this.adapterManager.addAdapter(GDRIVE_CODE, this);
    
    
    this.clientId = adapterConfigService.getConfigValue(new DMSAdapterConfigId(GDRIVE_CODE, "CLIENT_ID"));
    this.projectId = adapterConfigService.getConfigValue(new DMSAdapterConfigId(GDRIVE_CODE, "PROJECT_ID"));;
    this.authUri = adapterConfigService.getConfigValue(new DMSAdapterConfigId(GDRIVE_CODE, "AUTH_URI"));;
    this.tokenUri = adapterConfigService.getConfigValue(new DMSAdapterConfigId(GDRIVE_CODE, "TOKEN_URI"));;
    this.authProviderX509CertUrl = adapterConfigService.getConfigValue(new DMSAdapterConfigId(GDRIVE_CODE, "AUTH_PROVIDER_X509_CERT_URL"));;
    this.clientSecret = adapterConfigService.getConfigValue(new DMSAdapterConfigId(GDRIVE_CODE, "CLIENT_SECRET"));;
    this.callbackUri = adapterConfigService.getConfigValue(new DMSAdapterConfigId(GDRIVE_CODE, "CALLBACK_URI"));;
  }

  /**
   * build the client detail, and use it to construct GoogleClientSecrets.
   * Build the flow.
   * @throws Exception
   */
  @PostConstruct
  public void init() throws Exception {
    extensionType.put(EXPORT_DOC_TYPE, ".docx");
    extensionType.put(EXPORT_SPREADSHEET_TYPE, ".xlsx");
    extensionType.put(EXPORT_PRESENTATION_TYPE, ".pptx");
    extensionType.put(EXPORT_DRAWING_TYPE, ".pdf");

    downloadMimeMap.put(GOOGLE_DOCS, EXPORT_DOC_TYPE);
    downloadMimeMap.put(GOOGLE_SPREADSHEET, EXPORT_SPREADSHEET_TYPE);
    downloadMimeMap.put(GOOGLE_PRESENTATION, EXPORT_PRESENTATION_TYPE);
    downloadMimeMap.put(GOOGLE_DRAWING, EXPORT_DRAWING_TYPE);
    
    downloadFileType.put(GOOGLE_DOCS, DOCUMENT);
    downloadFileType.put(GOOGLE_SPREADSHEET, EXCEL);
    downloadFileType.put(GOOGLE_PRESENTATION, PPT);
    downloadFileType.put(GOOGLE_DRAWING, PDF);
    
    defaultFolders.put("root", "My Drive");
    defaultFolders.put("shared_drives", "Shared Drives");
    defaultFolders.put("sharedWithMe", "Shared With Me");
    
    // creating the Detail
    Details detail = new Details();
    detail.setClientId(clientId);
    detail.setAuthUri(authUri);
    detail.setTokenUri(tokenUri);
    detail.setClientSecret(clientSecret);
    detail.setRedirectUris(Arrays.asList(callbackUri));
    
    // creating the client secret
    GoogleClientSecrets secrets = new GoogleClientSecrets();
    secrets.setWeb(detail);
    
    flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, secrets, SCOPES)
        .setAccessType("offline")
        .build();
    
    GoogleAuthorizationCodeRequestUrl url = flow.newAuthorizationUrl();
    redirectUrl =  url.setRedirectUri(callbackUri)
        .set("prompt", "consent")
        .setAccessType("offline").build();
  }
  

  public List<FileInfoDTO> getFileList(Long userId, String folderId) throws Exception {
    try {
      Drive service = createDriveService(userId);
      List<FileInfoDTO> files = new ArrayList<>();
      if(StringUtils.isBlank(folderId)) {
        // check if access token is still valid
        service.about().get().setFields("user").execute();
        
        for(Map.Entry<String, String> entry : defaultFolders.entrySet())
        files.add(createFolder(entry.getKey(), entry.getValue()));
      } else if("shared_drives".equals(folderId)) {
        DriveList drives = service.drives().list()
            .setFields("nextPageToken, drives(id, name, kind)")
            .execute();

        drives.getDrives().forEach(drive -> {
          files.add(parseDriveToFileInfo(drive, folderId));
        });
      } else {
        String folderIdQuery = "sharedWithMe".equals(folderId) ?
            folderId : 
            String.format("'%s' in parents", escapeSpecialChars(folderId));
        FileList result = service.files().list()
            .setQ(folderIdQuery + " and trashed=false")
            .setFields("nextPageToken, files(id, name, mimeType, size, createdTime, modifiedTime)")
            .setSupportsAllDrives(true)
            .setIncludeItemsFromAllDrives(true)
            .setOrderBy("folder, name asc").execute();

        result.getFiles().forEach(file -> {
          files.add(parseFileToFileInfo(file, folderId));
        });
      }
      return files;
    } catch (HttpResponseException e) {
      throw handleGoogleException(e);
    }
  }
  
  @Override
  public FileInfoDTO getFileInfo(Long userId, String fileId) throws Exception {
    Drive service = createDriveService(userId);
    
    var file = getGoogleFileInfo(service, escapeSpecialChars(fileId));
    
    return parseFileToFileInfo(file, null);
  }
  
  @Override
  public void downloadFile(long userId, HttpServletResponse response, FileInfoDTO fileInfo)
      throws Exception {
    
    try(var outputStream = response.getOutputStream()) {
      String fileId = fileInfo.getId();
      
      Drive service = createDriveService(userId);
      var file = getGoogleFileInfo(service, escapeSpecialChars(fileId));
      
      // get info from google drive to avoid error if file was updated or info sent was tampered.
      String fileName = file.getName();
      String mimeType = file.getMimeType();
      
      response.setContentType(Objects.requireNonNullElse(downloadMimeMap.get(mimeType), mimeType));
      response.setHeader("Content-Disposition", "attachment; filename=\""+ fileName + "\"");
 
      String downloadMimeType = downloadMimeMap.get(mimeType);
      if(StringUtils.isNotBlank(downloadMimeType)) {
        service.files()
        .export(fileId, downloadMimeType)
        .executeMediaAndDownloadTo(outputStream);
      } else {
        service.files()
        .get(fileId)
        .executeMediaAndDownloadTo(outputStream);
      }
    } catch (HttpResponseException e) {
      throw handleGoogleException(e);
    }
  }
  
  @Override
  public void uploadFile(long userId, MultipartFile[] files, String parentId) throws Exception {
    try {
      Drive service = createDriveService(userId);
      for (MultipartFile file : files) {
        File fileMetadata = new File();
        fileMetadata.setName(file.getOriginalFilename());
        fileMetadata.setParents(List.of(parentId));
        InputStreamContent mediaContent = new InputStreamContent(file.getContentType(), file.getInputStream());
        
        // check first if folder is not binned before uploading
        getGoogleFileInfo(service, parentId);
        
        service.files()
            .create(fileMetadata, mediaContent)
            .setSupportsAllDrives(true)
            .execute();
     }
    } catch (GoogleJsonResponseException e) {
      System.out.println("hello: " + e.getStatusCode());
      throw e;
    }
    
  }
  
  @Override
  public void createFolder(long userId, String parentId, String folderName) throws Exception {
    File metaDataFolder = new File();
    metaDataFolder.setName(folderName);
    metaDataFolder.setParents(List.of(parentId));
    metaDataFolder.setMimeType(MIMETYPE_FOLDER);
    
    try {
      Drive service = createDriveService(userId);
      File folder = service.files()
      .create(metaDataFolder)
      .setSupportsAllDrives(true)
      .setFields("id")
      .execute();
      System.out.println(folder);
    } catch(GoogleJsonResponseException e) {
      throw e;
    }
    
  }

  @Override
  public List<String> getFolderNames(long userId, String parentId) throws Exception {
    Drive service = createDriveService(userId);
   
    String folderIdQuery = String.format("'%s' in parents and ", parentId);
    FileList result = service.files().list()
        .setQ(folderIdQuery + "trashed=false and mimeType = 'application/vnd.google-apps.folder'")
        .setFields("nextPageToken, files(name)")
        .execute();

    return result.getFiles().stream()
        .map(File::getName)
        .collect(Collectors.toList());
  }
  
  @Override
  public String getRedirectURL() {
    return redirectUrl;
  }

  @Override
  public String saveAuthorizationCode(Long id, String code) throws Exception {
    GoogleTokenResponse response = flow.newTokenRequest(code).setRedirectUri(callbackUri)
        .execute();

    JSONObject json = new JSONObject(response);
    json.put(EXPIRES_IN, getDateInMillisFromExpiresIn(json.getLong(EXPIRES_IN)));
    saveToken(id, json);
    return code;
  }
  
  /**
   * creates the drive service
   * @return
   * @throws Exception 
   */
  private Drive createDriveService(Long id) throws Exception {
    HttpCredentialsAdapter adapter = new HttpCredentialsAdapter(
        getCredential(getJsonAdapterInfo(new DMSAdapterInfoId(GDRIVE_CODE, id)), id));

    Drive service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, adapter)
        .setApplicationName(APPLICATION_NAME).build();

    return service;
  }

  /**
   * constructs the credentials and return it
   * @return
   * @throws IOException
   */
  private UserCredentials getCredential(JSONObject json, Long id) throws Exception {

    UserCredentials userCredentials = UserCredentials.newBuilder()
        .setClientId(clientId)
        .setClientSecret(clientSecret)
        .setAccessToken(
            new AccessToken(json.getString(ACCESS_TOKEN), new Date(json.getLong(EXPIRES_IN))))
        .setRefreshToken(json.getString(REFRESH_TOKEN))
        .build();
    
    // refresh the access token to check its validity
    refreshToken(userCredentials, id, json);
    return userCredentials;
  }
  
  /**
   * tries to refresh the token if it is expiring or has already expired
   * @param userCredentials
   * @param id
   * @param json
   * @return
   * @throws Exception
   */
  private boolean refreshToken(UserCredentials userCredentials, Long id,
      JSONObject json) throws Exception {

    if (userCredentials != null) {
      userCredentials.refreshIfExpired();
      
      // check if access token has been replaced to replace the access_token from json
      if(!json.getString(ACCESS_TOKEN).equals(userCredentials.getAccessToken().getTokenValue())) {
        json.put(ACCESS_TOKEN, userCredentials.getAccessToken().getTokenValue());
        json.put(EXPIRES_IN, userCredentials.getAccessToken().getExpirationTime().getTime());

        saveToken(id, json);
      }
      
      return true;
    }
    return false;
  }

  /**
   * saves the data in DMS_ADAPTER_INFO table
   * @param id
   * @param code
   * @throws Exception
   */
  private void saveToken(Long id, JSONObject json) throws Exception {
    DMSAdapterInfoEntity adapterInfo = new DMSAdapterInfoEntity();
    adapterInfo.setAdapterName(GDRIVE_CODE);
    adapterInfo.setUserId(id);
    adapterInfo.setAuthInfo(json.toString());

    adapterInfoService.save(adapterInfo);
  }
  
  private File getGoogleFileInfo(Drive service, String folderId) throws Exception {
    File file = service.files().get(folderId)
        .setFields("id, name, mimeType, size, createdTime, modifiedTime, trashed")
        .setSupportsAllDrives(true)
        .execute();
    if (file.getTrashed()) {// should not access folder/file that is binned
      throw new FileNotFoundException(String.format("File not found: %s", folderId));
    }
    return file;
  }
  
  
  /**
   * creates folder entity
   * @param id
   * @param folderName
   * @return
   */
  private FileInfoDTO createFolder(String id, String folderName) {
    FileInfoDTO fileInfo = new FileInfoDTO();
    fileInfo.setId(id);
    fileInfo.setName(folderName);
    fileInfo.setMimeType(MIMETYPE_FOLDER);
    fileInfo.setFileType("folder");
    return fileInfo;
  }
  

  /**
   * sets the FileInfo using the information from Google File
   * @param file
   * @param folderId
   * @return
   */
  private FileInfoDTO parseFileToFileInfo(File file, String folderId) {
    String mimeType = file.getMimeType();
    String name = file.getName();
    
    // if a supported google file, append the supposed conversion filetype to the name
    String conversionFileType = downloadFileType.get(mimeType);
    if(StringUtils.isNotBlank(conversionFileType)) {
      name += "."+conversionFileType;
    } else { // check if file extension matches the mimetype, if not, then append the correct mimetype
      name = addExtensionIfIncorrect(name, mimeType);
    }
    
    FileInfoDTO fileInfo = new FileInfoDTO();
    fileInfo.setId(file.getId());
    
    fileInfo.setName(name);
    fileInfo.setMimeType(mimeType);
    
    fileInfo.setFileType(
        MIMETYPE_FOLDER.equals(mimeType) ? 
            FOLDER : // if folder, filetype is folder
            getExtensionFromFileName(name)
        );
    
    
    
    fileInfo.setParentId(folderId);
    fileInfo.setSize(file.getSize());
    fileInfo.setCreateDt(convertDatetimetoDate(file.getCreatedTime()));
    fileInfo.setModifiedDt(convertDatetimetoDate(file.getModifiedTime()));
    
    return fileInfo;
  }
  
  private FileInfoDTO parseDriveToFileInfo(com.google.api.services.drive.model.Drive drive, String parentFolderId) {
    FileInfoDTO fileInfo = new FileInfoDTO();
    String name = drive.getName();
    fileInfo.setId(drive.getId());
    fileInfo.setName(name);
    fileInfo.setParentId(parentFolderId);
    fileInfo.setMimeType(MIMETYPE_FOLDER);
    fileInfo.setFileType(FOLDER);
    return fileInfo;
  }
  
  /**
   * Method to extract file extension from file name
   * @param fileName
   * @return
   */
  private String getExtensionFromFileName(String fileName) {
    if (StringUtils.isNotBlank(fileName)) {
      int lastDotIndex = fileName.lastIndexOf('.');
      
      //make sure that dot exists, and it is not in the last part of the filename
      if (lastDotIndex != -1 && lastDotIndex != fileName.length() - 1) {
        return fileName.substring(lastDotIndex + 1);
      }
    }
    return null;
  }
  
  /**
   * in google drive, when you upload new version, it will not change the file extension
   * so the extension for that new version will be wrong if the new file version's extension 
   * does not match the previous version's, and it will be treated the wrong way, need to 
   * append the correct file extension.
   * @param fileExtension
   * @param fileName
   * @param mimeType
   * @return
   */
  private String addExtensionIfIncorrect(String fileName, String mimeType) {
    String fileExtension = getExtensionFromFileName(fileName);
    if(StringUtils.isNotBlank(fileExtension)) {
      List<String> detectedExtensions = new ArrayList<>(Arrays.asList(MimeTypes.findExtensionsByMimeTypes(mimeType, false)));
      
      // if the file extension is not present in the detected extensions, 
      // that means mimetype and file extension is a mismatch
      if(!detectedExtensions.contains(fileExtension)) {
        // there are mimetypes that can have multiple extensions (ex: image/jpeg => [.jpg, .jpeg, .jpe])
        // in this case, we can just use the first value.
        fileName += "." + detectedExtensions.get(0);
      }
    }
    
    return fileName;
  }

  /**
   * gets the time when the token will expire based on the current time plus the 
   * expires_in in milliseconds
   * @param sec
   * @return
   */
  private long getDateInMillisFromExpiresIn(Long sec) {
    return System.currentTimeMillis() + (sec * 1000);
  }

  /**
   * Returns the json string containing the Auth Info of user
   * used for accessing the DMS
   * @param id
   * @return
   */
  private JSONObject getJsonAdapterInfo(DMSAdapterInfoId id) {
    return stringToJson(adapterInfoService.getAuthInfo(id).getAuthInfo());
  }
  
  /**
   * converts string into json
   * @param json
   * @return
   */
  private JSONObject stringToJson(String json) {
    return new JSONObject(json);
  }
  
 /**
  * converts google DateTime to util.date
  * @param date
  * @return
  */
 private Date convertDatetimetoDate(DateTime date) {
   return new Date(date.getValue());
 }
 
 /**
  * Custom escape special characters method for Google Drive API
  * @param input
  * @return
  */
 private String escapeSpecialChars(String input) {
   // escape both backslashes and single quotes
   return input.replaceAll("([\\\\'])", "\\\\$1");
 }
 
 /**
  * Handles the httpresponse exception
  * TODO
  * @param e
  * @return
  */
 private Exception handleGoogleException(HttpResponseException e) {
   var statusCode = e.getStatusCode();
   String message = e.getMessage();
   String content = e.getContent();
   String errorMsg = extractErrorMessage(content);
   
   switch (statusCode) {
   case HttpStatus.SC_BAD_REQUEST:
     if(message.contains("invalid_grant")) {
       LOG.error("Bad Request[Invalid Grant] ({}): {}", statusCode, errorMsg);
     } else {
       LOG.error("Bad Request ({}): {}", statusCode, errorMsg);
     }
     return e;
     
   case HttpStatus.SC_UNAUTHORIZED:
     LOG.warn("Unauthorized Error ({}): {}", statusCode, errorMsg);
   
   default:
     return e;
   }
 }
 
 /**
  * processes the Message of error from google drive api
  */
private String extractErrorMessage(String responseContent) {
  try {
    JSONObject responseObject = new JSONObject(responseContent);
    return responseObject.getString("error_description");
  } catch (Exception e) {
    LOG.error("Parsing Error on content: {}", responseContent);
    return responseContent;
  }
}

}
