/*
 * Copyright (c) 2XXX 11MadScientist
 * All Rights Reserved.
 * 
 * Project Name: dms-service-repositories
 */
package dev.madscientist.proj.dms.dmsadapters;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

/**
 * Manages all the DMS Adapters.
 * 
 * <b>File Created</b>: Feb 21, 2024
 * 
 * @author 11MadScientist
 */

@Component
public class AdapterManager {
  private final Map<String, DMSAdapter> adapterList = new ConcurrentHashMap<>();

  public AdapterManager() {

  }

  public void addAdapter(String code, DMSAdapter adapter) {
    adapterList.put(code, adapter);
  }

  public void removeAdapter(String code) {
    adapterList.remove(code);
  }

  /**
   * will determine what dms adapter to use based on the given code
   * 
   * @param code
   * @return
   */
  public DMSAdapter getAdapter(String code) {
    return adapterList.get(code);
  }

}
