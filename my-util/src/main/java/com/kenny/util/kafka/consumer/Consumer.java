package com.kenny.util.kafka.consumer;

public interface Consumer {
    void receiveMsg(MessageListener listener);

    void receiveMsgBatch(BatchMessageListener listener);

    void close();
}
