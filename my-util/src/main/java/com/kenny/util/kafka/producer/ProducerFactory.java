package com.kenny.util.kafka.producer;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.kenny.util.kafka.config.ProducerConfig;
import com.kenny.util.kafka.config.ProducerConfigs;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class ProducerFactory {
    private static final Logger log = LoggerFactory.getLogger(ProducerFactory.class);
    private Map<String, IProducer> producerMap = Maps.newConcurrentMap();
    private final ProducerConfigs producerConfigs;

    public ProducerFactory(ProducerConfigs producerConfigs) {
        Preconditions.checkNotNull(producerConfigs, "kafkaConfig is null");
        this.producerConfigs = producerConfigs;
    }

    public IProducer getProducer(String topic) {
        if (this.producerMap.containsKey(topic)) {
            return (IProducer)this.producerMap.get(topic);
        } else {
            Preconditions.checkNotNull(this.producerConfigs.getProducerConfig(topic), "there's no config for topic " + topic);
            IProducer producer = new CatEnhancedProducer(this.buildKafkaProducer(topic), this.producerConfigs.getProducerConfig(topic), topic);
            if (this.producerMap.putIfAbsent(topic, producer) != null) {
                log.info("producer has been created for topic {}", topic);
                producer.close();
            } else {
                log.info("trigger meta fetch for topic {}", topic);

                try {
                    producer.getPartitions();
                } catch (Exception var4) {
                    log.warn("{}", var4.getMessage());
                }
            }

            return (IProducer)this.producerMap.get(topic);
        }
    }

    private KafkaProducer<String, String> buildKafkaProducer(String topic) {
        ProducerConfig producerConfig = this.producerConfigs.getProducerConfig(topic);
        return new KafkaProducer(producerConfig.buildProperties(topic));
    }
}
