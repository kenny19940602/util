package com.kenny.util.kafka.consumer;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;

public class ConsumerKey {
    private final String consumerConfigId;
    private final String topic;
    private final String group;

    public ConsumerKey(String topic, String group, String consumerConfigId) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(topic), "topic is empty");
        Preconditions.checkArgument(StringUtils.isNotEmpty(group), "group is empty");
        this.topic = topic;
        this.group = group;
        this.consumerConfigId = consumerConfigId;
    }

    public ConsumerKey(String topic, String group) {
        this(topic, group, (String)null);
    }

    public String getConsumerConfigId() {
        return this.consumerConfigId;
    }

    public String getTopic() {
        return this.topic;
    }

    public String getGroup() {
        return this.group;
    }

    @Override
    public String toString() {
        return "ConsumerKey(consumerConfigId=" + this.getConsumerConfigId() + ", topic=" + this.getTopic() + ", group=" + this.getGroup() + ")";
    }
}
