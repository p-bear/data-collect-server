package com.pbear.datacollectserver.data.kafka;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class EventMessage<T> {
  private String transactionId;
  private String issuer;
  private Long timestamp;
  private T data;
}
