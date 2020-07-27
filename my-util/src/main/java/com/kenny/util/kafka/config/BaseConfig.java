package com.kenny.util.kafka.config;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;

public class BaseConfig {
    private final String servers;
    private final String clientId;

    BaseConfig(String servers, String clientId) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(servers), "kafka server列表为空");
        Preconditions.checkArgument(StringUtils.isNotEmpty(clientId), "kafka client id为空");
        Preconditions.checkArgument(Utils.checkServers(servers), "kafka server配置错误，非合法配置");
        this.servers = servers;
        this.clientId = clientId;
    }

    public String getServers() {
        return this.servers;
    }

    public String getClientId() {
        return this.clientId;
    }

    @Override
    public String toString() {
        return "BaseConfig(servers=" + this.getServers() + ", clientId=" + this.getClientId() + ")";
    }
}
