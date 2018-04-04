package com.simonjamesrowe.www.user.domain;

public class UserDto {

  private String name;
  private String emailAddress;
  private boolean admin;

  public UserDto(String name, String emailAddress, boolean admin) {
    this.name = name;
    this.emailAddress = emailAddress;
    this.admin = admin;
  }

  public String getName() {
    return name;
  }

  public String getEmailAddress() {
    return emailAddress;
  }

  public boolean isAdmin() {
    return admin;
  }
}
