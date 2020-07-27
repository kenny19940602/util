package com.kenny.util.nacos;

import com.alibaba.nacos.api.exception.NacosException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.wnameless.json.flattener.JsonFlattener;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class NacosConfigClient implements ConfigClient {
    private static final Logger log = LoggerFactory.getLogger(NacosConfigClient.class);
    private static final int NACOS_TIMEOUT = 5000;
    private static final int MAX_QUEUE_SIZE = 200;
    private final String dataId;
    private final NacosConfigFileClient nacosConfigFileClient;
    private volatile Map<String, Object> configs;
    private Yaml yaml = new Yaml();
    private ObjectMapper objectMapper = new ObjectMapper();
    private Executor executor;

    public NacosConfigClient(String dataId, NacosConfigFileClient nacosConfigFileClient) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(dataId), "dataId is empty");
        Preconditions.checkNotNull(nacosConfigFileClient, "nacosConfigFileClient is null");
        this.dataId = dataId;
        this.nacosConfigFileClient = nacosConfigFileClient;
        this.executor = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingDeque(200), (r) -> {
            return new Thread(r, "NacosConfigClient");
        });
    }

    @PostConstruct
    public void init() throws NacosException {
        DynamicConfig dynamicConfig = this.nacosConfigFileClient.getDynamicConfig(this.dataId, this.executor);
        this.configs = this.parseYaml(dynamicConfig.getContent(5000L));
        if (this.configs == null) {
            this.configs = Maps.newHashMap();
        }

        dynamicConfig.addListener(new ConfigListener() {
            public void onConfigUpdate(String content) {
                Map<String, Object> newValue = NacosConfigClient.this.parseYaml(content);
                if (newValue != null) {
                    NacosConfigClient.this.configs = newValue;
                }

            }
        });
    }

    private Map<String, Object> parseYaml(String configInfo) {
        if (StringUtils.isEmpty(configInfo)) {
            return null;
        } else {
            try {
                Map<String, Object> yamlValue = (Map)this.yaml.load(configInfo);
                if (MapUtils.isEmpty(yamlValue)) {
                    return yamlValue;
                } else {
                    String json = this.objectMapper.writeValueAsString(yamlValue);
                    return JsonFlattener.flattenAsMap(json);
                }
            } catch (JsonProcessingException var4) {
                log.error("", var4);
                return null;
            }
        }
    }

    private <T> T getOrDefault(String key, T defaultValue, Function<String, T> fun) {
        Map<String, Object> tmp = this.configs;
        if (MapUtils.isEmpty(tmp)) {
            return defaultValue;
        } else {
            Object value = tmp.getOrDefault(key, defaultValue);
            return value == null ? null : fun.apply(value.toString());
        }
    }

    public String getString(String key) {
        return this.getString(key, (String)null);
    }

    public String getString(String key, String defaultValue) {
        return (String)this.getOrDefault(key, defaultValue, (t) -> {
            return t;
        });
    }

    public Integer getInteger(String key) {
        return this.getInteger(key, (Integer)null);
    }

    public Integer getInteger(String key, Integer defaultValue) {
        return (Integer)this.getOrDefault(key, defaultValue, Integer::valueOf);
    }

    public Long getLong(String key) {
        return this.getLong(key, (Long)null);
    }

    public Long getLong(String key, Long defaultValue) {
        return (Long)this.getOrDefault(key, defaultValue, Long::valueOf);
    }

    public Boolean getBoolean(String key) {
        return this.getBoolean(key, (Boolean)null);
    }

    public Boolean getBoolean(String key, Boolean defaultValue) {
        return (Boolean)this.getOrDefault(key, defaultValue, Boolean::valueOf);
    }

    public Double getDouble(String key) {
        return this.getDouble(key, (Double)null);
    }

    public Double getDouble(String key, Double defaultValue) {
        return (Double)this.getOrDefault(key, defaultValue, Double::valueOf);
    }
}
