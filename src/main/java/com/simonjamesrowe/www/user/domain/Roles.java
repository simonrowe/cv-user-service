package com.simonjamesrowe.www.user.domain;

public enum Roles {
  ROLE_USER("ROLE_USER"),
  ROLE_ADMIN("ROLE_ADMIN");

  private String roleName;

  private Roles(String roleName) {
    this.roleName = roleName;
  }

  public String getRoleName() {
    return roleName;
  }
}
