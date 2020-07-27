package com.kenny.util.nacos;

import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executor;

class NacosDynamicConfig implements DynamicConfig {
    private static final Logger log = LoggerFactory.getLogger(NacosDynamicConfig.class);
    private final ConfigService configService;
    private final String dataId;
    private final String group;
    private final Executor executor;

    NacosDynamicConfig(ConfigService configService, String dataId, String group, Executor executor) {
        Preconditions.checkNotNull(configService, "configService is null");
        Preconditions.checkArgument(StringUtils.isNotEmpty(dataId), "dataId is empty");
        Preconditions.checkArgument(StringUtils.isNotEmpty(group), "group is empty");
        log.info("dataId {}, group {}", dataId, group);
        this.dataId = dataId;
        this.group = group;
        this.configService = configService;
        this.executor = executor;
    }

    @Override
    public String getContent(long timeoutMs) {
        log.info("timeoutMs: {}", timeoutMs);

        try {
            return this.configService.getConfig(this.dataId, this.group, timeoutMs);
        } catch (NacosException var4) {
            throw new NacosConfigException("fail to get config from nacos", var4);
        }
    }

    @Override
    public void addListener(ConfigListener configListener) {
        log.info("add config listener");
        Preconditions.checkNotNull(configListener, "configListener is null");
        Preconditions.checkNotNull(this.executor, "executor is null");

        try {
            this.configService.addListener(this.dataId, this.group, new Listener() {
                @Override
                public Executor getExecutor() {
                    return NacosDynamicConfig.this.executor;
                }

                @Override
                public void receiveConfigInfo(String configInfo) {
                    if (NacosDynamicConfig.log.isDebugEnabled()) {
                        NacosDynamicConfig.log.debug("get new configInfo: {}", configInfo);
                    } else {
                        NacosDynamicConfig.log.info("get new configInfo");
                    }

                    configListener.onConfigUpdate(configInfo);
                }
            });
        } catch (NacosException var3) {
            throw new NacosConfigException("fail to add listener", var3);
        }
    }
}
