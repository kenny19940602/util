package com.kenny.util.nacos;

public interface ConfigListener {
    void onConfigUpdate(String content);
}
