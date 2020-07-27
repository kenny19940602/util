package com.kenny.util.kafka.consumer;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.kenny.util.kafka.config.ConsumerConfig;
import com.kenny.util.kafka.config.ConsumerConfigs;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Properties;

public class ConsumerFactory {
    private final ConsumerConfigs consumerConfigs;

    public ConsumerFactory(ConsumerConfigs consumerConfigs) {
        Preconditions.checkNotNull(consumerConfigs, "consumerConfigs is null");
        this.consumerConfigs = consumerConfigs;
    }

    public Consumer getConsumer(ConsumerKey consumerKey) {
        Preconditions.checkNotNull(consumerKey, "consumerKey is null");
        ConsumerConfig consumerConfig = this.consumerConfigs.getConsumerConfig(consumerKey.getConsumerConfigId());
        Preconditions.checkNotNull(consumerKey, "there is no config for consumer config id " + consumerKey.getConsumerConfigId());
        KafkaConsumer<String, String> kafkaConsumer = this.buildKafkaConsumer(consumerKey, consumerConfig);
        return new DefaultConsumer(kafkaConsumer, consumerKey, consumerConfig);
    }

    KafkaConsumer<String, String> buildKafkaConsumer(ConsumerKey consumerKey, ConsumerConfig consumerConfig) {
        Properties properties = consumerConfig.buildProperties(consumerKey);
        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer(properties);
        kafkaConsumer.subscribe(Lists.newArrayList(new String[]{consumerKey.getTopic()}));
        return kafkaConsumer;
    }
}
