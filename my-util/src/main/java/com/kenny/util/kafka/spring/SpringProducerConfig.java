package com.kenny.util.kafka.spring;

public class SpringProducerConfig {
    private String servers;
    private String clientId;
    private Integer acks;
    private Integer maxBlockMs;

    public SpringProducerConfig() {
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

    public void setServers(final String servers) {
        this.servers = servers;
    }

    public void setClientId(final String clientId) {
        this.clientId = clientId;
    }

    public void setAcks(final Integer acks) {
        this.acks = acks;
    }

    public void setMaxBlockMs(final Integer maxBlockMs) {
        this.maxBlockMs = maxBlockMs;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof SpringProducerConfig)) {
            return false;
        } else {
            SpringProducerConfig other = (SpringProducerConfig)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                label59: {
                    Object this$servers = this.getServers();
                    Object other$servers = other.getServers();
                    if (this$servers == null) {
                        if (other$servers == null) {
                            break label59;
                        }
                    } else if (this$servers.equals(other$servers)) {
                        break label59;
                    }

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

                Object this$acks = this.getAcks();
                Object other$acks = other.getAcks();
                if (this$acks == null) {
                    if (other$acks != null) {
                        return false;
                    }
                } else if (!this$acks.equals(other$acks)) {
                    return false;
                }

                Object this$maxBlockMs = this.getMaxBlockMs();
                Object other$maxBlockMs = other.getMaxBlockMs();
                if (this$maxBlockMs == null) {
                    if (other$maxBlockMs != null) {
                        return false;
                    }
                } else if (!this$maxBlockMs.equals(other$maxBlockMs)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(final Object other) {
        return other instanceof SpringProducerConfig;
    }

    @Override
    public int hashCode() {
        int result = 1;
        Object $servers = this.getServers();
        result = result * 59 + ($servers == null ? 43 : $servers.hashCode());
        Object $clientId = this.getClientId();
        result = result * 59 + ($clientId == null ? 43 : $clientId.hashCode());
        Object $acks = this.getAcks();
        result = result * 59 + ($acks == null ? 43 : $acks.hashCode());
        Object $maxBlockMs = this.getMaxBlockMs();
        result = result * 59 + ($maxBlockMs == null ? 43 : $maxBlockMs.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "SpringProducerConfig(servers=" + this.getServers() + ", clientId=" + this.getClientId() + ", acks=" + this.getAcks() + ", maxBlockMs=" + this.getMaxBlockMs() + ")";
    }
}
