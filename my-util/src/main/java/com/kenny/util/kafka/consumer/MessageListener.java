package com.kenny.util.kafka.consumer;

public interface MessageListener {
    Status consume(KafkaMessage message);
}
