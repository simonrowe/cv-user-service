package com.simonjamesrowe.www.user.security;

import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.UrlJwkProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.simonjamesrowe.www.user.domain.Roles;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.net.URL;
import java.security.interfaces.RSAPublicKey;
import java.util.*;
import java.util.stream.Collectors;

@Configuration
public class JwtTokenManager implements InitializingBean {

  public static final String EMAIL = "email";
  public static final String NAME = "name";

  @Value("${google.issuer}")
  private String issuer;

  @Value("${google.jwkUrl}")
  private String jwkUrl;

  @Autowired private ClientRegistrationRepository clientRegistrationRepository;

  private Map<String, RsaVerifier> rsaVerifiers = new HashMap<>();

  private static final String ADMINISTRATOR = "simon.rowe@gmail.com";

  /**
   * public String generateToken(OAuth2User user) { String name = (String)
   * user.getAttributes().getHeadline(NAME); String emailAddress = (String)
   * user.getAttributes().getHeadline(EMAIL); List<String> roles = getRoles(emailAddress); return
   * Jwts.builder() .claim(EMAIL, emailAddress) .claim(ROLES, roles) .claim(NAME, name)
   * .setExpiration(new Date(System.currentTimeMillis() + (expiryTime * 1000)))
   * .signWith(SignatureAlgorithm.HS512, secretKey.getBytes()) .compact(); }*
   */
  public OAuth2AuthenticationToken decodeToken(String idToken) throws Exception {
    try {
      String kid = JwtHelper.headers(idToken).get("kid");
      final Jwt tokenDecoded = JwtHelper.decodeAndVerify(idToken, verifier(kid));
      final Map<String, String> authInfo =
          new ObjectMapper().readValue(tokenDecoded.getClaims(), Map.class);
      verifyClaims(authInfo);

      Map<String, Object> oauth2UserProperties = new HashMap<>();
      oauth2UserProperties.put(EMAIL, authInfo.get(EMAIL));
      List<String> roles = getRoles(authInfo.get(EMAIL));
      oauth2UserProperties.put(NAME, authInfo.get(NAME));
      OAuth2User user =
          new DefaultOAuth2User(
              roles.stream().map(r -> new SimpleGrantedAuthority(r)).collect(Collectors.toSet()),
              oauth2UserProperties,
              EMAIL);
      OAuth2AuthenticationToken oAuthToken =
          new OAuth2AuthenticationToken(
              user,
              user.getAuthorities(),
              SecurityConfiguration.GOOGLE_OAUTH2_CLIENT_REGISTRATION_ID);
      oAuthToken.setAuthenticated(true);
      return oAuthToken;
    } catch (Exception e) {
      return null;
    }
  }

  public void verifyClaims(Map claims) {
    int exp = (int) claims.get("exp");
    Date expireDate = new Date(exp * 1000L);
    Date now = new Date();
    if (expireDate.before(now)
        || !claims.get("iss").equals(issuer)
        || !claims
            .get("aud")
            .equals(
                clientRegistrationRepository
                    .findByRegistrationId(
                        SecurityConfiguration.GOOGLE_OAUTH2_CLIENT_REGISTRATION_ID)
                    .getClientId())) {
      throw new RuntimeException("Invalid claims");
    }
  }

  private RsaVerifier verifier(String kid) throws Exception {
    if (rsaVerifiers.containsKey(kid)) {
      return rsaVerifiers.get(kid);
    }
    JwkProvider provider = new UrlJwkProvider(new URL(jwkUrl));
    Jwk jwk = provider.get(kid);
    rsaVerifiers.put(kid, new RsaVerifier((RSAPublicKey) jwk.getPublicKey()));
    return rsaVerifiers.get(kid);
  }

  private List<String> getRoles(String emailAddress) {
    if (emailAddress.equalsIgnoreCase(ADMINISTRATOR)) {
      return Arrays.asList(Roles.ROLE_USER.getRoleName(), Roles.ROLE_ADMIN.getRoleName());
    }
    return Arrays.asList(Roles.ROLE_USER.getRoleName());
  }

  @Override
  public void afterPropertiesSet() throws Exception {}
}
