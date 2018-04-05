package com.simonjamesrowe.www.user.rest;

import com.simonjamesrowe.www.user.domain.Roles;
import com.simonjamesrowe.www.user.domain.UserDto;
import com.simonjamesrowe.www.user.security.JwtTokenManager;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class UserEndpoint {

  @GetMapping("/user")
  public UserDto getUser(Principal principal) {
    OAuth2AuthenticationToken authenticationToken = (OAuth2AuthenticationToken) principal;
    return new UserDto(
        (String) authenticationToken.getPrincipal().getAttributes().get(JwtTokenManager.NAME),
        (String) authenticationToken.getPrincipal().getAttributes().get(JwtTokenManager.EMAIL),
        authenticationToken
            .getAuthorities()
            .stream()
            .anyMatch(r -> r.getAuthority().equalsIgnoreCase(Roles.ROLE_ADMIN.getRoleName())));
  }
}
