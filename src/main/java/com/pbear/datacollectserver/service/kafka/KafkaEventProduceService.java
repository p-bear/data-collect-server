package com.pbear.datacollectserver.service.kafka;

import com.pbear.datacollectserver.data.kafka.EventMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaEventProduceService {
  private final KafkaTemplate<String, EventMessage> kafkaTemplate;

  @Value("${server.name}")
  private String serverName;

  public <V> ListenableFuture<SendResult<String, EventMessage>> sendSimpleMessage(
      final String topic, final EventMessage<V> eventMessage) {
    if (eventMessage.getTransactionId() == null) {
      eventMessage.setTransactionId(UUID.randomUUID().toString());
    }
    if (eventMessage.getIssuer() == null) {
      eventMessage.setIssuer(serverName);
    }
    if (eventMessage.getTimestamp() == null) {
      eventMessage.setTimestamp(new Date().getTime());
    }

    log.debug("send message, topic: {}, message class: {}, message: {}", topic, eventMessage.getData().getClass().getName(), eventMessage);
    return this.kafkaTemplate.send(topic, eventMessage);
  }
}
