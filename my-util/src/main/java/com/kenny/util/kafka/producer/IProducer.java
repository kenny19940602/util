package com.kenny.util.kafka.producer;

import com.google.common.util.concurrent.FutureCallback;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.common.PartitionInfo;

import java.util.List;

public interface IProducer {
    SendResult send(String message) throws KafkaProducerException;

    SendResult send(String key, String message) throws KafkaProducerException;

    void send(String message, FutureCallback<SendResult> futureCallback);

    void send(String key, String message, FutureCallback<SendResult> futureCallback);

    Producer<String, String> getKafkaProducer();

    void close();

    List<PartitionInfo> getPartitions();

    void flush();
}
