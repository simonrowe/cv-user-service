package com.simonjamesrowe.www.user.domain;

import javax.validation.constraints.NotEmpty;

public class HeadlineDto {

  @NotEmpty private String headline;

  public HeadlineDto() {}

  public HeadlineDto(String headline) {
    this.headline = headline;
  }

  public String getHeadline() {
    return headline;
  }

  public void setHeadline(String headline) {
    this.headline = headline;
  }
}
