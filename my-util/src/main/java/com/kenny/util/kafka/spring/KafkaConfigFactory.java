package com.kenny.util.kafka.spring;

import com.google.common.base.Preconditions;
import com.kenny.util.kafka.config.ConsumerConfig;
import com.kenny.util.kafka.config.ConsumerConfigs;
import com.kenny.util.kafka.config.ProducerConfig;
import com.kenny.util.kafka.config.ProducerConfigs;
import com.kenny.util.kafka.config.ProducerConfig.ProducerConfigBuilder;
import com.kenny.util.kafka.config.ConsumerConfig.ConsumerConfigBuilder;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.context.ApplicationContext;

public class KafkaConfigFactory {
    private static final Logger log = LoggerFactory.getLogger(KafkaConfigFactory.class);
    @Autowired
    private ApplicationContext applicationContext;
    @Value("${spring.application.name}")
    private String applicationName;

    public KafkaConfigFactory() {
    }

    public ProducerConfigs build(SpringProducerConfigs springProducerConfigs) {
        Preconditions.checkNotNull(springProducerConfigs, "springProducerConfigs is null");
        log.info("kafka producer config {}", springProducerConfigs);
        ProducerConfigs producerConfigs = new ProducerConfigs(this.buildDefaultProducer(springProducerConfigs.getDefaultConfig()));
        if (MapUtils.isNotEmpty(springProducerConfigs.getCustoms())) {
            springProducerConfigs.getCustoms().forEach((t, c) -> {
                ProducerConfig producerConfig = this.buildProducer(springProducerConfigs.getDefaultConfig(), c);
                producerConfigs.put(t, producerConfig);
            });
        }

        log.info("ProducerConfigs {}", producerConfigs);
        return producerConfigs;
    }

    private ProducerConfig buildDefaultProducer(SpringProducerConfig springProducerConfig) {
        if (springProducerConfig == null) {
            return null;
        } else {
            try {
                ProducerConfigBuilder builder = new ProducerConfigBuilder();
                return builder.setServers(springProducerConfig.getServers()).setClientId(this.buildClientId(springProducerConfig.getClientId())).setMaxBlockMs(springProducerConfig.getMaxBlockMs()).setAcks(springProducerConfig.getAcks()).build();
            } catch (Exception var3) {
                log.info("cannot build default producer config, {}", var3.getMessage());
                return null;
            }
        }
    }

    private ProducerConfig buildProducer(SpringProducerConfig defaultConfig, SpringProducerConfig custom) {
        ProducerConfigBuilder builder = new ProducerConfigBuilder();
        if (defaultConfig != null) {
            builder.setServers(defaultConfig.getServers()).setClientId(this.buildClientId(defaultConfig.getClientId())).setMaxBlockMs(defaultConfig.getMaxBlockMs()).setAcks(defaultConfig.getAcks());
        }

        if (StringUtils.isNotEmpty(custom.getServers())) {
            builder.setServers(custom.getServers());
        }

        if (StringUtils.isNotEmpty(custom.getClientId())) {
            builder.setClientId(custom.getClientId());
        }

        if (custom.getAcks() != null) {
            builder.setAcks(custom.getAcks());
        }

        if (custom.getMaxBlockMs() != null) {
            builder.setMaxBlockMs(custom.getMaxBlockMs());
        }

        return builder.build();
    }

    public ConsumerConfigs build(SpringConsumerConfigs springConsumerConfigs) {
        Preconditions.checkNotNull(springConsumerConfigs, "springConsumerConfigs is null");
        log.info("kafka consumer config {}", springConsumerConfigs);
        ConsumerConfigs consumerConfigs = new ConsumerConfigs(this.buildDefaultConsumer(springConsumerConfigs.getDefaultConfig()));
        if (MapUtils.isNotEmpty(springConsumerConfigs.getCustoms())) {
            springConsumerConfigs.getCustoms().forEach((t, c) -> {
                ConsumerConfig consumerConfig = this.buildConsumer(springConsumerConfigs.getDefaultConfig(), c);
                consumerConfigs.put(t, consumerConfig);
            });
        }

        log.info("ConsumerConfigs {}", consumerConfigs);
        return consumerConfigs;
    }

    private ConsumerConfig buildDefaultConsumer(SpringConsumerConfig springConsumerConfig) {
        if (springConsumerConfig == null) {
            return null;
        } else {
            try {
                ConsumerConfigBuilder builder = new ConsumerConfigBuilder();
                return builder.setServers(springConsumerConfig.getServers()).setClientId(this.buildClientId(springConsumerConfig.getClientId())).setBatchRecvTimeoutMs(springConsumerConfig.getBatchRecvTimeoutMs()).setBatchSize(springConsumerConfig.getBatchSize()).setRetryBackMs(springConsumerConfig.getRetryBackMs()).setRetryTimes(springConsumerConfig.getRetryTimes()).build();
            } catch (Exception var3) {
                log.info("cannot build default consumer config, {}", var3.getMessage());
                return null;
            }
        }
    }

    private ConsumerConfig buildConsumer(SpringConsumerConfig defaultConfig, SpringConsumerConfig springConsumerConfig) {
        ConsumerConfigBuilder builder = new ConsumerConfigBuilder();
        if (defaultConfig != null) {
            builder.setServers(springConsumerConfig.getServers()).setClientId(this.buildClientId(springConsumerConfig.getClientId())).setBatchRecvTimeoutMs(springConsumerConfig.getBatchRecvTimeoutMs()).setBatchSize(springConsumerConfig.getBatchSize()).setRetryBackMs(springConsumerConfig.getRetryBackMs()).setRetryTimes(springConsumerConfig.getRetryTimes());
        }

        if (StringUtils.isNotEmpty(springConsumerConfig.getServers())) {
            builder.setServers(springConsumerConfig.getServers());
        }

        if (StringUtils.isNotEmpty(springConsumerConfig.getClientId())) {
            builder.setClientId(springConsumerConfig.getClientId());
        }

        if (springConsumerConfig.getBatchRecvTimeoutMs() != null) {
            builder.setBatchRecvTimeoutMs(springConsumerConfig.getBatchRecvTimeoutMs());
        }

        if (springConsumerConfig.getBatchSize() != null) {
            builder.setBatchSize(springConsumerConfig.getBatchSize());
        }

        if (springConsumerConfig.getRetryBackMs() != null) {
            builder.setRetryBackMs(springConsumerConfig.getRetryBackMs());
        }

        if (springConsumerConfig.getRetryTimes() != null) {
            builder.setRetryTimes(springConsumerConfig.getRetryTimes());
        }

        return builder.build();
    }

    private String buildClientId(String clientId) {
        return StringUtils.isEmpty(clientId) ? this.buildDefaultClientId() : clientId;
    }

    private String buildDefaultClientId() {
        Registration registration = this.getRegistration();
        if (registration != null) {
            log.info("create client id from registration, {}", registration);
            return registration.getServiceId() + "_" + registration.getHost();
        } else {
            log.info("user application name as client id, {}", this.applicationName);
            return this.applicationName;
        }
    }

    private Registration getRegistration() {
        try {
            return (Registration)this.applicationContext.getBean(Registration.class);
        } catch (Exception var2) {
            log.info("no Registration, {}", var2.getMessage());
            return null;
        }
    }
}
