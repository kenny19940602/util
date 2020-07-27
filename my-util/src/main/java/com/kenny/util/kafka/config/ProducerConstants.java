package com.kenny.util.kafka.config;

public interface ProducerConstants {
    String SERVERS = "bootstrap.servers";
    String ACKS = "acks";
    String MAX_BLOCK_MS = "max.block.ms";
    String CLIENT_ID = "client.id";
    String KEY_SERIALIZER = "key.serializer";
    String VALUE_SERIALIZER = "value.serializer";
}
