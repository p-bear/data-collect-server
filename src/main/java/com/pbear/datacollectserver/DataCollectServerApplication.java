package com.pbear.datacollectserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DataCollectServerApplication {

  public static void main(String[] args) {
    SpringApplication.run(DataCollectServerApplication.class, args);
  }

}
