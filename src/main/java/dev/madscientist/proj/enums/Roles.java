/*
 * Copyright (c) 2XXX 11MadScientist
 * All Rights Reserved.
 * 
 * Project Name: dms-service-repositories
 */
package dev.madscientist.proj.enums;

/**
 * <b>File Created</b>: Feb 26, 2024
 * 
 * @author 11MadScientist
 */
public enum Roles {
  SUPER_ADMINISTRATOR("SA"), ADMINISTRATOR("A"), GENERAL_USER("GU");

  private final String name;

  private Roles(String s) {
    name = s;
  }

  public boolean equalsName(String other) {
    return name.equals(other);
  }

  public String toString() {
    return this.name;
  }
}
