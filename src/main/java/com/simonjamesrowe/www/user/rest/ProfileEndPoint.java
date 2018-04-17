package com.simonjamesrowe.www.user.rest;

import com.simonjamesrowe.www.user.domain.HeadlineDto;
import com.simonjamesrowe.www.user.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProfileEndPoint {

  @Autowired private ProfileService profileService;

  @GetMapping(value = "profile/headline")
  public HeadlineDto getHeadline() {
    return profileService.getHeadline();
  }

  @PostMapping(value = "profile/headline/save", consumes = "application/json")
  public void headline(@RequestBody HeadlineDto headlineDto) {
    profileService.saveHeadline(headlineDto.getHeadline());
  }

  @GetMapping(value = "profile/description")
  public String getDescription() {
    return profileService.getDescription();
  }

  @PostMapping(value = "profile/description/save")
  public void saveDescription(@RequestBody String description) {
    profileService.saveDescription(description);
  }
}
