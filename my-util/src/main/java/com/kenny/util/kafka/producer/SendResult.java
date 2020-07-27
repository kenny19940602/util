package com.kenny.util.kafka.producer;

public interface SendResult {
    long offset();

    int partition();

    long timestamp();

    String topic();
}