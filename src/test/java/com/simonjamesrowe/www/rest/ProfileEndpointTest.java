package com.simonjamesrowe.www.rest;

import com.simonjamesrowe.www.RedisTestConfiguration;
import com.simonjamesrowe.www.user.CvUserServiceApplication;
import com.simonjamesrowe.www.user.domain.HeadlineDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(
  webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
  classes = {RedisTestConfiguration.class, CvUserServiceApplication.class}
)
public class ProfileEndpointTest {

  @Autowired private TestRestTemplate restTemplate;

  @Test
  public void testInit() {
    assertNotNull(restTemplate);
  }

  @Test
  public void getHeadline() {
    ResponseEntity<HeadlineDto> responseEntity =
        this.restTemplate.getForEntity("/profile/headline", HeadlineDto.class);
    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    assertEquals(
        "AGILE SOFTWARE ENGINEER – UTILISING JAVA, SPRING, ANGULAR & AWS TO ARCHITECT AND DELIVER ENTERPRISE SOFTWARE SOLUTIONS",
        responseEntity.getBody().getHeadline());
  }
}
