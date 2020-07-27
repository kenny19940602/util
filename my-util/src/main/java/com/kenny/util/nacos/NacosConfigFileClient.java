package com.kenny.util.nacos;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.Executor;

public class NacosConfigFileClient {
    private static final Logger log = LoggerFactory.getLogger(NacosConfigFileClient.class);
    private String group;
    private final ConfigService configService;

    public NacosConfigFileClient(String namespace, String group, String server) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(group), "group为空");
        Preconditions.checkArgument(StringUtils.isNotEmpty(server), "server为空");
        log.info("namespace {}, group {}, server {}", new Object[]{namespace, group, server});
        Properties properties = new Properties();
        properties.put("serverAddr", server);
        if (StringUtils.isNotEmpty(namespace)) {
            properties.put("namespace", namespace);
        }

        try {
            this.configService = NacosFactory.createConfigService(properties);
        } catch (NacosException var6) {
            throw new NacosConfigException("fail to create nacos config service", var6);
        }

        this.group = group;
    }

    public DynamicConfig getDynamicConfig(String dataId, Executor executor) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(dataId), "dataId is empty");
        log.info("get nacos dynamic config for dataId {}", dataId);
        return new NacosDynamicConfig(this.configService, dataId, this.group, executor);
    }
}