package com.pbear.datacollectserver.data.blizzard.call.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GetAuctionsCommoditiesRes {
  private List<Auction> auctions;

  @Getter
  @Setter
  public static class Auction {
    private Long id;
    private Item item;
    private Long quantity;
    private Long unitPrice;
    private String timeLeft;

    @Getter
    @Setter
    public static class Item {
      private Long id;
    }
  }
}
