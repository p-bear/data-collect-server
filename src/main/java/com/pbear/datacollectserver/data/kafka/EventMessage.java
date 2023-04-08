package com.pbear.datacollectserver.data.kafka;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class EventMessage<T> {
  private String id;
  private String issuer;
  private Long timestamp;
  private Long version;
  private T data;
}
