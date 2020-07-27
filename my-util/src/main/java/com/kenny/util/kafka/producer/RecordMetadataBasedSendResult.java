package com.kenny.util.kafka.producer;

import com.google.common.base.Preconditions;
import org.apache.kafka.clients.producer.RecordMetadata;

public class RecordMetadataBasedSendResult implements SendResult {
    private final RecordMetadata recordMetadata;

    public RecordMetadataBasedSendResult(RecordMetadata recordMetadata) {
        Preconditions.checkNotNull(recordMetadata, "recordMetadata is null");
        this.recordMetadata = recordMetadata;
    }

    public long offset() {
        return this.recordMetadata.offset();
    }

    public int partition() {
        return this.recordMetadata.partition();
    }

    public long timestamp() {
        return this.recordMetadata.timestamp();
    }

    public String topic() {
        return this.recordMetadata.topic();
    }
}
