package com.kenny.util.nacos;

public interface DynamicConfig {
    String getContent(long timeoutMs);

    void addListener(ConfigListener configListener);
}
