package com.kenny.util.kafka.config;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public class ConsumerConfigs {
    private ConsumerConfig defaultConsumerConfig;
    private Map<String, ConsumerConfig> consumerConfigMap = Maps.newHashMap();

    public ConsumerConfigs(ConsumerConfig defaultConsumerConfig) {
        this.defaultConsumerConfig = defaultConsumerConfig;
    }

    public void put(String consumerId, ConsumerConfig consumerConfig) {
        this.consumerConfigMap.put(consumerId, consumerConfig);
    }

    public ConsumerConfig getConsumerConfig(String consumerId) {
        return StringUtils.isNoneEmpty(new CharSequence[]{consumerId}) && this.consumerConfigMap.containsKey(consumerId) ? (ConsumerConfig)this.consumerConfigMap.get(consumerId) : this.defaultConsumerConfig;
    }
}
