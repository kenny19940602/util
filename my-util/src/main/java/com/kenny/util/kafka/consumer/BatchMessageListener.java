package com.kenny.util.kafka.consumer;

import java.util.List;

public interface BatchMessageListener {
    Status consume(List<KafkaMessage> message);
}
