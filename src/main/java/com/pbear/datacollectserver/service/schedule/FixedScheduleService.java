package com.pbear.datacollectserver.service.schedule;

import com.pbear.datacollectserver.service.blizzard.WowDataCollectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FixedScheduleService {
  private final WowDataCollectService wowDataCollectService;

  @Scheduled(cron = "0 10 * * * ?")
  public void collectWowAuctionData() {
    log.info("collect wow auctionData start");
    this.wowDataCollectService.publishWowAuctionData();
    log.info("collect wow auctionData end");
  }
}
