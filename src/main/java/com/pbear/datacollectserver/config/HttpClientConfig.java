package com.pbear.datacollectserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.http.HttpClient;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class HttpClientConfig {
  private final ExecutorService executorService =
      Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);

  @Bean
  public HttpClient httpClient() {
    return HttpClient.newBuilder()
        .followRedirects(HttpClient.Redirect.ALWAYS)
        .connectTimeout(Duration.of(10, ChronoUnit.SECONDS))
        .executor(this.executorService)
        .build();
  }
}
