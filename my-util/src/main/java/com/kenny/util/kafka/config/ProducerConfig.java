package com.kenny.util.kafka.config;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class ProducerConfig extends BaseConfig{
    private int acks = -1;
    private int maxBlockMs = 100;

    public ProducerConfig(String servers, String clientId) {
        super(servers, clientId);
    }

    public Properties buildProperties(String topic) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(topic), "topic is empty");
        Properties properties = new Properties();
        properties.put("bootstrap.servers", this.getServers());
        properties.put("acks", String.valueOf(this.getAcks()));
        properties.put("client.id", topic + "#" + this.getClientId());
        properties.put("max.block.ms", String.valueOf(this.getMaxBlockMs()));
        properties.put("key.serializer", StringSerializer.class);
        properties.put("value.serializer", StringSerializer.class);
        return properties;
    }

    public void setAcks(int acks) {
        Preconditions.checkArgument(acks >= -1 && acks <= 1, "acks should be -1,0,1");
        this.acks = acks;
    }

    public void setMaxBlockMs(int maxBlockMs) {
        Preconditions.checkArgument(maxBlockMs >= 5, "maxBlockMs >= 5");
        this.maxBlockMs = maxBlockMs;
    }

    public int getAcks() {
        return this.acks;
    }

    public int getMaxBlockMs() {
        return this.maxBlockMs;
    }

    @Override
    public String toString() {
        return "ProducerConfig(acks=" + this.getAcks() + ", maxBlockMs=" + this.getMaxBlockMs() + ")";
    }

    public static class ProducerConfigBuilder {
        private String servers;
        private String clientId;
        private Integer acks;
        private Integer maxBlockMs;

        public ProducerConfigBuilder() {
        }

        public ProducerConfigBuilder setServers(String servers) {
            this.servers = servers;
            return this;
        }

        public ProducerConfigBuilder setClientId(String clientId) {
            this.clientId = clientId;
            return this;
        }

        public ProducerConfigBuilder setAcks(Integer acks) {
            this.acks = acks;
            return this;
        }

        public ProducerConfigBuilder setMaxBlockMs(Integer maxBlockMs) {
            this.maxBlockMs = maxBlockMs;
            return this;
        }

        public ProducerConfig build() {
            ProducerConfig producerConfig = new ProducerConfig(this.servers, this.clientId);
            if (this.acks != null) {
                producerConfig.setAcks(this.acks);
            }

            if (this.maxBlockMs != null) {
                producerConfig.setMaxBlockMs(this.maxBlockMs);
            }

            return producerConfig;
        }

        public String getServers() {
            return this.servers;
        }

        public String getClientId() {
            return this.clientId;
        }

        public Integer getAcks() {
            return this.acks;
        }

        public Integer getMaxBlockMs() {
            return this.maxBlockMs;
        }
    }
}
