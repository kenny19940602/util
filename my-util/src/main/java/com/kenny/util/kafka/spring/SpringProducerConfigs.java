package com.kenny.util.kafka.spring;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.Map;

@ConfigurationProperties(
        prefix = "paas.kafka.producer",
        ignoreUnknownFields = false
)
public class SpringProducerConfigs {
    @NestedConfigurationProperty
    private SpringProducerConfig defaultConfig;
    private Map<String, SpringProducerConfig> customs;

    public SpringProducerConfigs() {
    }

    public SpringProducerConfig getDefaultConfig() {
        return this.defaultConfig;
    }

    public Map<String, SpringProducerConfig> getCustoms() {
        return this.customs;
    }

    public void setDefaultConfig(final SpringProducerConfig defaultConfig) {
        this.defaultConfig = defaultConfig;
    }

    public void setCustoms(final Map<String, SpringProducerConfig> customs) {
        this.customs = customs;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof SpringProducerConfigs)) {
            return false;
        } else {
            SpringProducerConfigs other = (SpringProducerConfigs)o;
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
        return other instanceof SpringProducerConfigs;
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
        return "SpringProducerConfigs(defaultConfig=" + this.getDefaultConfig() + ", customs=" + this.getCustoms() + ")";
    }
}
