package com.kenny.util.kafka.spring;

public class SpringConsumerConfig {
    private String servers;
    private String clientId;
    private Integer batchSize;
    private Integer batchRecvTimeoutMs;
    private Integer retryBackMs;
    private Integer retryTimes;

    public SpringConsumerConfig() {
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

    public void setServers(final String servers) {
        this.servers = servers;
    }

    public void setClientId(final String clientId) {
        this.clientId = clientId;
    }

    public void setBatchSize(final Integer batchSize) {
        this.batchSize = batchSize;
    }

    public void setBatchRecvTimeoutMs(final Integer batchRecvTimeoutMs) {
        this.batchRecvTimeoutMs = batchRecvTimeoutMs;
    }

    public void setRetryBackMs(final Integer retryBackMs) {
        this.retryBackMs = retryBackMs;
    }

    public void setRetryTimes(final Integer retryTimes) {
        this.retryTimes = retryTimes;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof SpringConsumerConfig)) {
            return false;
        } else {
            SpringConsumerConfig other = (SpringConsumerConfig)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                Object this$servers = this.getServers();
                Object other$servers = other.getServers();
                if (this$servers == null) {
                    if (other$servers != null) {
                        return false;
                    }
                } else if (!this$servers.equals(other$servers)) {
                    return false;
                }

                Object this$clientId = this.getClientId();
                Object other$clientId = other.getClientId();
                if (this$clientId == null) {
                    if (other$clientId != null) {
                        return false;
                    }
                } else if (!this$clientId.equals(other$clientId)) {
                    return false;
                }

                Object this$batchSize = this.getBatchSize();
                Object other$batchSize = other.getBatchSize();
                if (this$batchSize == null) {
                    if (other$batchSize != null) {
                        return false;
                    }
                } else if (!this$batchSize.equals(other$batchSize)) {
                    return false;
                }

                label62: {
                    Object this$batchRecvTimeoutMs = this.getBatchRecvTimeoutMs();
                    Object other$batchRecvTimeoutMs = other.getBatchRecvTimeoutMs();
                    if (this$batchRecvTimeoutMs == null) {
                        if (other$batchRecvTimeoutMs == null) {
                            break label62;
                        }
                    } else if (this$batchRecvTimeoutMs.equals(other$batchRecvTimeoutMs)) {
                        break label62;
                    }

                    return false;
                }

                label55: {
                    Object this$retryBackMs = this.getRetryBackMs();
                    Object other$retryBackMs = other.getRetryBackMs();
                    if (this$retryBackMs == null) {
                        if (other$retryBackMs == null) {
                            break label55;
                        }
                    } else if (this$retryBackMs.equals(other$retryBackMs)) {
                        break label55;
                    }

                    return false;
                }

                Object this$retryTimes = this.getRetryTimes();
                Object other$retryTimes = other.getRetryTimes();
                if (this$retryTimes == null) {
                    if (other$retryTimes != null) {
                        return false;
                    }
                } else if (!this$retryTimes.equals(other$retryTimes)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(final Object other) {
        return other instanceof SpringConsumerConfig;
    }

    @Override
    public int hashCode() {
        int result = 1;
        Object $servers = this.getServers();
        result = result * 59 + ($servers == null ? 43 : $servers.hashCode());
        Object $clientId = this.getClientId();
        result = result * 59 + ($clientId == null ? 43 : $clientId.hashCode());
        Object $batchSize = this.getBatchSize();
        result = result * 59 + ($batchSize == null ? 43 : $batchSize.hashCode());
        Object $batchRecvTimeoutMs = this.getBatchRecvTimeoutMs();
        result = result * 59 + ($batchRecvTimeoutMs == null ? 43 : $batchRecvTimeoutMs.hashCode());
        Object $retryBackMs = this.getRetryBackMs();
        result = result * 59 + ($retryBackMs == null ? 43 : $retryBackMs.hashCode());
        Object $retryTimes = this.getRetryTimes();
        result = result * 59 + ($retryTimes == null ? 43 : $retryTimes.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "SpringConsumerConfig(servers=" + this.getServers() + ", clientId=" + this.getClientId() + ", batchSize=" + this.getBatchSize() + ", batchRecvTimeoutMs=" + this.getBatchRecvTimeoutMs() + ", retryBackMs=" + this.getRetryBackMs() + ", retryTimes=" + this.getRetryTimes() + ")";
    }
}
