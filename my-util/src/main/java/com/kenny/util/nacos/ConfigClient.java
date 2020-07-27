package com.kenny.util.nacos;

public interface ConfigClient {
    String getString(String key);

    String getString(String key, String defaultValue);

    Integer getInteger(String key);

    Integer getInteger(String key, Integer defaultValue);

    Long getLong(String key);

    Long getLong(String key, Long defaultValue);

    Boolean getBoolean(String key);

    Boolean getBoolean(String key, Boolean defaultValue);

    Double getDouble(String key);

    Double getDouble(String key, Double defaultValue);
}
