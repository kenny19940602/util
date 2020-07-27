package com.kenny.util.kafka.spring;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.Map;

@ConfigurationProperties(
        prefix = "paas.kafka.consumer",
        ignoreUnknownFields = false
)
public class SpringConsumerConfigs {
    @NestedConfigurationProperty
    private SpringConsumerConfig defaultConfig;
    private Map<String, SpringConsumerConfig> customs;

    public SpringConsumerConfigs() {
    }

    public SpringConsumerConfig getDefaultConfig() {
        return this.defaultConfig;
    }

    public Map<String, SpringConsumerConfig> getCustoms() {
        return this.customs;
    }

    public void setDefaultConfig(final SpringConsumerConfig defaultConfig) {
        this.defaultConfig = defaultConfig;
    }

    public void setCustoms(final Map<String, SpringConsumerConfig> customs) {
        this.customs = customs;
    }

    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof SpringConsumerConfigs)) {
            return false;
        } else {
            SpringConsumerConfigs other = (SpringConsumerConfigs)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                Object this$defaultConfig = this.getDefaultConfig();
                Object other$defaultConfig = other.getDefaultConfig();
                if (this$defaultConfig == null) {
                    if (other$defaultConfig != null) {
                        return false;
                    }
                } else if (!this$defaultConfig.equals(other$defaultConfig)) {
                    return false;
                }

                Object this$customs = this.getCustoms();
                Object other$customs = other.getCustoms();
                if (this$customs == null) {
                    if (other$customs != null) {
                        return false;
                    }
                } else if (!this$customs.equals(other$customs)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(final Object other) {
        return other instanceof SpringConsumerConfigs;
    }

    @Override
    public int hashCode() {
        int result = 1;
        Object $defaultConfig = this.getDefaultConfig();
        result = result * 59 + ($defaultConfig == null ? 43 : $defaultConfig.hashCode());
        Object $customs = this.getCustoms();
        result = result * 59 + ($customs == null ? 43 : $customs.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "SpringConsumerConfigs(defaultConfig=" + this.getDefaultConfig() + ", customs=" + this.getCustoms() + ")";
    }
}
