package com.pbear.datacollectserver;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBatchProcessing
public class DataCollectServerApplication {

  public static void main(String[] args) {
    SpringApplication.run(DataCollectServerApplication.class, args);
  }

}
