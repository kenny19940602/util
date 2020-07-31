package com.kenny.util.redis;

public interface KeyPrefix {

    int expireSeconds();

    String getKeyPrefix();
}
