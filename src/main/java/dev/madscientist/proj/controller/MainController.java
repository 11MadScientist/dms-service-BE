/*
 * Copyright (c) 2XXX 11MadScientist
 * All Rights Reserved.
 * 
 * Project Name: dms-service-repositories
 */
package dev.madscientist.proj.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.madscientist.proj.services.DMSAdapterService;

/**
 * <b>File Created</b>: Feb 21, 2024
 * 
 * @author 11MadScientist
 */

@RestController
public class MainController {

  private final DMSAdapterService adapterSvc;

  public MainController(DMSAdapterService adapterSvc) {
    this.adapterSvc = adapterSvc;
  }

  @GetMapping("/oauth")
  public String saveAuthorizationCode(@RequestParam("code") String code)
      throws Exception {

    return adapterSvc.saveAuthorizationCode("GD", code);
  }
}
