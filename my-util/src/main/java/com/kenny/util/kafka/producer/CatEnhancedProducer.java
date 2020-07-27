package com.kenny.util.kafka.producer;

import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.FutureCallback;
import com.kenny.util.kafka.config.ProducerConfig;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.PartitionInfo;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class CatEnhancedProducer implements IProducer {
    private final KafkaProducer<String, String> kafkaProducer;
    private final ProducerConfig producerConfig;
    private final String topic;

    public CatEnhancedProducer(KafkaProducer<String, String> kafkaProducer, ProducerConfig producerConfig, String topic) {
        Preconditions.checkNotNull(kafkaProducer, "kafkaProducer is null");
        Preconditions.checkNotNull(producerConfig, "producerConfig is null");
        Preconditions.checkArgument(StringUtils.isNoneEmpty(new CharSequence[]{topic}), "topic is empty");
        this.kafkaProducer = kafkaProducer;
        this.producerConfig = producerConfig;
        this.topic = topic;
    }

    @Override
    public SendResult send(String message) throws KafkaProducerException {
        return this.send((String)null, (String)message);
    }

    @Override
    public SendResult send(String key, String message) throws KafkaProducerException {
        long begin = System.currentTimeMillis();
        Future<RecordMetadata> future = this.kafkaProducer.send(this.buildProducerRecord(key, message));
        long end = System.currentTimeMillis();

        try {
            return new RecordMetadataBasedSendResult((RecordMetadata)future.get(toWaitTime((long)this.producerConfig.getMaxBlockMs(), end - begin), TimeUnit.MILLISECONDS));
        } catch (ExecutionException var9) {
            throw new KafkaProducerException("fail to send message: " + message, var9.getCause());
        } catch (Exception var10) {
            throw new KafkaProducerException("fail to send message: " + message, var10);
        }
    }

    private static long toWaitTime(long maxBlockMs, long hasSpend) {
        long rest = maxBlockMs - hasSpend;
        return rest < 0L ? 0L : rest;
    }

    @Override
    public void send(String message, FutureCallback<SendResult> futureCallback) {
        this.send((String)null, message, futureCallback);
    }

    @Override
    public void send(String key, String message, FutureCallback<SendResult> futureCallback) {
        this.kafkaProducer.send(this.buildProducerRecord(key, message), new Callback() {
            @Override
            public void onCompletion(RecordMetadata metadata, Exception exception) {
                if (metadata != null) {
                    futureCallback.onSuccess(new RecordMetadataBasedSendResult(metadata));
                } else {
                    futureCallback.onFailure(exception);
                }

            }
        });
    }

    @Override
    public Producer<String, String> getKafkaProducer() {
        return this.kafkaProducer;
    }

    @Override
    public void close() {
        this.kafkaProducer.close();
    }

    @Override
    public List<PartitionInfo> getPartitions() {
        return this.kafkaProducer.partitionsFor(this.topic);
    }

    @Override
    public void flush() {
        this.kafkaProducer.flush();
    }

    private ProducerRecord<String, String> buildProducerRecord(String key, String message) {
        return new ProducerRecord(this.topic, key, message);
    }
}
