package com.kenny.util.kafka.consumer;

import com.google.common.base.Preconditions;
import org.apache.kafka.clients.consumer.ConsumerRecord;

public class KafkaMessage {
    private final ConsumerRecord<String, String> consumerRecord;

    KafkaMessage(ConsumerRecord<String, String> consumerRecord) {
        Preconditions.checkNotNull(consumerRecord, "consumerRecord is null");
        this.consumerRecord = consumerRecord;
    }

    public ConsumerRecord<String, String> consumerRecord() {
        return this.consumerRecord;
    }

    public String getMessage() {
        return (String)this.consumerRecord.value();
    }

    public String getKey() {
        return (String)this.consumerRecord.key();
    }

    public String getTopic() {
        return this.consumerRecord.topic();
    }

    @Override
    public String toString() {
        return "topic:" + this.getTopic() + ", key:" + this.getKey() + ", message:" + this.getMessage();
    }
}
