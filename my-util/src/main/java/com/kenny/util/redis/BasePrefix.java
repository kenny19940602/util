package com.kenny.util.redis;

/**
 * ClassName: BasePrefix
 * Function:  KeyPrefix的抽象类
 * Date:      2019/10/17 15:45
 * @author     Kenny
 * version    V1.0
 */
public abstract class BasePrefix implements KeyPrefix {

    private int expireSeconds;
    private String keyPrefix;

    protected BasePrefix(String keyPrefix) {
        this(0, keyPrefix);
    }

    protected BasePrefix(int expireSeconds, String keyPrefix) {
        this.expireSeconds = expireSeconds;
        this.keyPrefix = keyPrefix;
    }

    /**
     * 默认0代表永不过期
     * @return
     */
    @Override
    public int expireSeconds() {
        return expireSeconds;
    }

    @Override
    public String getKeyPrefix() {
        String className = getClass().getSimpleName();
        return className + ":" + keyPrefix;
    }
}
