package com.kenny.util.kafka.spring;

import com.kenny.util.kafka.config.ConsumerConfigs;
import com.kenny.util.kafka.config.ProducerConfigs;
import com.kenny.util.kafka.consumer.ConsumerFactory;
import com.kenny.util.kafka.producer.ProducerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({SpringProducerConfigs.class, SpringConsumerConfigs.class})
public class KafkaBootstrap {
    public KafkaBootstrap() {
    }

    @Bean
    public KafkaConfigFactory kafkaConfigFactory() {
        return new KafkaConfigFactory();
    }

    @Bean
    public ProducerFactory producerFactory(KafkaConfigFactory kafkaConfigFactory, SpringProducerConfigs springProducerConfigs) {
        ProducerConfigs producerConfigs = kafkaConfigFactory.build(springProducerConfigs);
        return new ProducerFactory(producerConfigs);
    }

    @Bean
    public ConsumerFactory consumerFactory(KafkaConfigFactory kafkaConfigFactory, SpringConsumerConfigs springConsumerConfigs) {
        ConsumerConfigs consumerConfigs = kafkaConfigFactory.build(springConsumerConfigs);
        return new ConsumerFactory(consumerConfigs);
    }
}
