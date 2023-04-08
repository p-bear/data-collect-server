package com.pbear.datacollectserver.service.blizzard;

import com.pbear.datacollectserver.data.blizzard.call.response.GetAuctionsRes;
import com.pbear.datacollectserver.data.blizzard.call.response.GetRealmRes;
import com.pbear.datacollectserver.data.kafka.EventMessage;
import com.pbear.datacollectserver.service.kafka.KafkaEventProduceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class WowDataCollectService {
  private final BlizzardApiService blizzardApiService;
  private final KafkaEventProduceService kafkaEventProduceService;

  public void publishWowAuctionData() {
    GetRealmRes getRealmRes;
    try {
      getRealmRes = this.blizzardApiService.getRealm("hyjal");
    } catch (IOException | InterruptedException e) {
      log.error("fail to get hyjal realm from blizzard", e);
      throw new RuntimeException(e);
    }

    String connectedRealm = getRealmRes.getConnectedRealm().getHref()
        .split("/connected-realm/")[1]
        .split("\\?")[0];
    GetAuctionsRes getAuctionsRes;
    try {
      getAuctionsRes = this.blizzardApiService.getAuctions(Long.parseLong(connectedRealm));
    } catch (IOException | InterruptedException e) {
      log.error("fail to get Auction data from blizzard", e);
      throw new RuntimeException(e);
    }

    this.kafkaEventProduceService.sendSimpleMessage("test.message", EventMessage.builder()
        .data(getAuctionsRes)
        .build());
  }
}
