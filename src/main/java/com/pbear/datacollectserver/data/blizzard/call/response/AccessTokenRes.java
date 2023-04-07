package com.pbear.datacollectserver.data.blizzard.call.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class AccessTokenRes {
  private String accessToken;
  private String tokenType;
  private LocalDateTime issueDate;
  private LocalDateTime expiredDate;
  private String sub;
}
