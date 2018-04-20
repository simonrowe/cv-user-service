package com.simonjamesrowe.www;

import org.springframework.stereotype.Component;
import redis.embedded.RedisServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;

@Component
public class RedisTestConfiguration {

  private RedisServer redisServer;

  @PostConstruct
  public void startRedis() throws IOException {
    redisServer = new RedisServer(6379);
    redisServer.start();
  }

  @PreDestroy
  public void stopRedis() {
    redisServer.stop();
  }
}
