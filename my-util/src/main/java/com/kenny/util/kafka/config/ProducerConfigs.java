package com.kenny.util.kafka.config;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public class ProducerConfigs {
    private ProducerConfig defaultProducerConfig;
    private Map<String, ProducerConfig> producerConfigMap = Maps.newHashMap();

    public ProducerConfigs(ProducerConfig defaultProducerConfig) {
        this.defaultProducerConfig = defaultProducerConfig;
    }

    public void put(String topic, ProducerConfig ProducerConfig) {
        this.producerConfigMap.put(topic, ProducerConfig);
    }

    public ProducerConfig getProducerConfig(String topic) {
        Preconditions.checkArgument(StringUtils.isNoneEmpty(new CharSequence[]{topic}), "topic是空字符串");
        return this.producerConfigMap.containsKey(topic) ? (ProducerConfig)this.producerConfigMap.get(topic) : this.defaultProducerConfig;
    }

    @Override
    public String toString() {
        return "ProducerConfigs(defaultProducerConfig=" + this.defaultProducerConfig + ", producerConfigMap=" + this.producerConfigMap + ")";
    }
}
