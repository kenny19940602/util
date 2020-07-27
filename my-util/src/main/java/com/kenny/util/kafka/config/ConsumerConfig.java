package com.kenny.util.kafka.config;

import com.google.common.base.Preconditions;
import com.kenny.util.kafka.consumer.ConsumerKey;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Properties;

public class ConsumerConfig extends BaseConfig{
    private int batchSize = 100;
    private int batchRecvTimeoutMs = 1000;
    private int retryBackMs = 50;
    private int retryTimes = 3;

    public ConsumerConfig(String servers, String clientId) {
        super(servers, clientId);
    }

    public void setBatchSize(int batchSize) {
        Preconditions.checkArgument(batchSize >= 1, "batchSize < 1");
        this.batchSize = batchSize;
    }

    public void setBatchRecvTimeoutMs(int batchRecvTimeoutMs) {
        Preconditions.checkArgument(batchRecvTimeoutMs >= 1, "batchRecvTimeoutMs < 1");
        this.batchRecvTimeoutMs = batchRecvTimeoutMs;
    }

    public void setRetryBackMs(int retryBackMs) {
        Preconditions.checkArgument(retryBackMs >= 0, "retryBackMs < 0");
        this.retryBackMs = retryBackMs;
    }

    public void setRetryTimes(int retryTimes) {
        Preconditions.checkArgument(retryTimes >= 0, "retryTimes < 0");
        this.retryTimes = retryTimes;
    }

    public Properties buildProperties(ConsumerKey consumerKey) {
        Preconditions.checkNotNull(consumerKey, "consumerKey is null");
        Properties properties = new Properties();
        properties.put("bootstrap.servers", this.getServers());
        properties.put("client.id", consumerKey.getTopic() + "#" + consumerKey.getGroup() + "#" + this.getClientId());
        properties.put("group.id", consumerKey.getGroup());
        properties.put("key.deserializer", StringDeserializer.class);
        properties.put("value.deserializer", StringDeserializer.class);
        return properties;
    }

    public int getBatchSize() {
        return this.batchSize;
    }

    public int getBatchRecvTimeoutMs() {
        return this.batchRecvTimeoutMs;
    }

    public int getRetryBackMs() {
        return this.retryBackMs;
    }

    public int getRetryTimes() {
        return this.retryTimes;
    }

    @Override
    public String toString() {
        return "ConsumerConfig(super=" + super.toString() + ", batchSize=" + this.getBatchSize() + ", batchRecvTimeoutMs=" + this.getBatchRecvTimeoutMs() + ", retryBackMs=" + this.getRetryBackMs() + ", retryTimes=" + this.getRetryTimes() + ")";
    }

    public static class ConsumerConfigBuilder {
        private String servers;
        private String clientId;
        private Integer batchSize;
        private Integer batchRecvTimeoutMs;
        private Integer retryBackMs;
        private Integer retryTimes;

        public ConsumerConfigBuilder() {
        }

        public ConsumerConfigBuilder setServers(String servers) {
            this.servers = servers;
            return this;
        }

        public ConsumerConfigBuilder setClientId(String clientId) {
            this.clientId = clientId;
            return this;
        }

        public ConsumerConfigBuilder setBatchSize(Integer batchSize) {
            this.batchSize = batchSize;
            return this;
        }

        public ConsumerConfigBuilder setBatchRecvTimeoutMs(Integer batchRecvTimeoutMs) {
            this.batchRecvTimeoutMs = batchRecvTimeoutMs;
            return this;
        }

        public ConsumerConfigBuilder setRetryBackMs(Integer retryBackMs) {
            this.retryBackMs = retryBackMs;
            return this;
        }

        public ConsumerConfigBuilder setRetryTimes(Integer retryTimes) {
            this.retryTimes = retryTimes;
            return this;
        }

        public ConsumerConfig build() {
            ConsumerConfig consumerConfig = new ConsumerConfig(this.servers, this.clientId);
            if (this.batchSize != null) {
                consumerConfig.setBatchSize(this.batchSize);
            }

            if (this.batchRecvTimeoutMs != null) {
                consumerConfig.setBatchRecvTimeoutMs(this.batchRecvTimeoutMs);
            }

            if (this.retryBackMs != null) {
                consumerConfig.setRetryBackMs(this.retryBackMs);
            }

            if (this.retryTimes != null) {
                consumerConfig.setRetryTimes(this.retryTimes);
            }

            return consumerConfig;
        }

        public String getServers() {
            return this.servers;
        }

        public String getClientId() {
            return this.clientId;
        }

        public Integer getBatchSize() {
            return this.batchSize;
        }

        public Integer getBatchRecvTimeoutMs() {
            return this.batchRecvTimeoutMs;
        }

        public Integer getRetryBackMs() {
            return this.retryBackMs;
        }

        public Integer getRetryTimes() {
            return this.retryTimes;
        }

        @Override
        public String toString() {
            return "ConsumerConfig.ConsumerConfigBuilder(servers=" + this.getServers() + ", clientId=" + this.getClientId() + ", batchSize=" + this.getBatchSize() + ", batchRecvTimeoutMs=" + this.getBatchRecvTimeoutMs() + ", retryBackMs=" + this.getRetryBackMs() + ", retryTimes=" + this.getRetryTimes() + ")";
        }
    }
}
