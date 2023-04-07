package com.pbear.datacollectserver.data.blizzard.call.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetRealmRes {
  private Long id;
  private Region region;
  private ConnectedRealm connectedRealm;
  private String name;
  private String category;
  private String locale;
  private String timezone;
  private Type type;
  private Boolean is_tournament;
  private String slug;


  @Getter
  @Setter
  public static class Region {
    private Long id;
    private Key key;
    private String name;

    @Getter
    @Setter
    public static class Key {
      private String href;
    }
  }

  @Getter
  @Setter
  public static class ConnectedRealm {
    private String href;
  }

  @Getter
  @Setter
  public static class Type {
    private String type;
    private String name;
  }
}
