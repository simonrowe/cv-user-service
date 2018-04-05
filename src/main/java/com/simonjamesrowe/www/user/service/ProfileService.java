package com.simonjamesrowe.www.user.service;

import com.simonjamesrowe.www.user.domain.HeadlineDto;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class ProfileService implements InitializingBean {

  private static final Logger LOG = LoggerFactory.getLogger(ProfileService.class);
  private static final String PROFILE_HEADLINE = "PROFILE_HEADLINE";

  @Autowired private StringRedisTemplate stringRedisTemplate;

  @Override
  public void afterPropertiesSet() throws Exception {
    // lets initialise some test data
    if (StringUtils.isBlank(get().getHeadline())) {
      save(IOUtils.toString(new ClassPathResource("headline.txt").getInputStream()));
    }
  }

  public HeadlineDto get() {
    return new HeadlineDto(stringRedisTemplate.opsForValue().get(PROFILE_HEADLINE));
  }

  public void save(String value) {
    stringRedisTemplate.opsForValue().set(PROFILE_HEADLINE, value);
  }
}
