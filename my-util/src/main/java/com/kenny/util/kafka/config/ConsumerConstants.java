package com.kenny.util.kafka.config;

public interface ConsumerConstants {
    String SERVERS = "bootstrap.servers";
    String GROUP = "group.id";
    String AUTO_COMMIT = "enable.auto.commit";
    String CLIENT_ID = "client.id";
    String KEY_SERIALIZER = "key.deserializer";
    String VALUE_SERIALIZER = "value.deserializer";
}
