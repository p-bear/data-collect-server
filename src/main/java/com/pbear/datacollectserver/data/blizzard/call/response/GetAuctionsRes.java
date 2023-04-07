package com.pbear.datacollectserver.data.blizzard.call.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GetAuctionsRes {
  private ConnectedRealm connectedRealm;
  private List<Auction> auctions;
  private Commodities commodities;

  @Getter
  @Setter
  public static class ConnectedRealm {
    private String href;
  }

  @Getter
  @Setter
  public static class Auction {
    private Long id;
    private Item item;
    private Long buyout;
    private Long quantity;
    private String timeLeft;

    @Getter
    @Setter
    public static class Item {
      private Long id;
      private Integer context;
      private List<Integer> bonusLists;
      private List<Modifier> modifiers;

      @Getter
      @Setter
      public static class Modifier {
        private String type;
        private String value;
      }
    }
  }

  @Getter
  @Setter
  public static class Commodities {
    private String href;
  }
}
