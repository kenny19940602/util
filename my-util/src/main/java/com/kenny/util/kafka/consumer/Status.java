package com.kenny.util.kafka.consumer;

public enum Status {
    SUCCESS,
    RECONSUME_LATER,
    FAIL_AND_SKIP;

    private Status() {
    }
}
