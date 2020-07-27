package com.kenny.util.kafka.consumer;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.kenny.util.kafka.config.ConsumerConfig;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class DefaultConsumer implements Consumer {
    private static final Logger log = LoggerFactory.getLogger(DefaultConsumer.class);
    private final KafkaConsumer<String, String> kafkaConsumer;
    private final ConsumerConfig config;
    private final ConsumerKey consumerKey;
    private volatile State state;
    private Thread thread;

    public DefaultConsumer(KafkaConsumer<String, String> kafkaConsumer, ConsumerKey consumerKey, ConsumerConfig config) {
        Preconditions.checkNotNull(kafkaConsumer, "kafkaConsumer is null");
        Preconditions.checkNotNull(config, "config is null");
        Preconditions.checkNotNull(consumerKey, "consumerKey is null");
        this.kafkaConsumer = kafkaConsumer;
        this.config = config;
        this.state = State.INIT;
        this.consumerKey = consumerKey;
    }

    @Override
    public synchronized void receiveMsg(MessageListener listener) {
        Preconditions.checkNotNull(listener, "listener is null");
        if (this.state != State.INIT) {
            throw new IllegalStateException("current state is " + this.state);
        } else {
            this.thread = this.createThread(new MessageListenerRunner(listener));
            this.thread.start();
            this.state = State.REGISTERED;
            log.info("state into {}", this.state);
        }
    }


    @Override
    public synchronized void receiveMsgBatch(BatchMessageListener listener) {
        Preconditions.checkNotNull(listener, "listener is null");
        if (this.state != State.INIT) {
            throw new IllegalStateException("current state is " + this.state);
        } else {
            this.thread = this.createThread(new BatchMessageListenerRunner(listener));
            this.thread.start();
            this.state = State.REGISTERED;
            log.info("state into {}", this.state);
        }
    }

    @Override
    public synchronized void close() {
        if (this.state == State.CLOSED) {
            log.info("consumer has been closed");
        } else {
            this.state = State.CLOSED;
            this.kafkaConsumer.wakeup();
        }
    }

    private Thread createThread(Runnable r) {
        Thread thread = new Thread(r);
        thread.setName("kafka consumer thread-" + this.consumerKey);
        return thread;
    }

    private void consumeRecord(Object record, Supplier<Status> supplier) {
        try {
            int retryTimes = 0;

            while(this.state != State.CLOSED) {
                Status status = (Status)supplier.get();
                log.debug("status = {}", status);
                if (status != Status.FAIL_AND_SKIP || retryTimes >= this.config.getRetryTimes()) {
                    this.kafkaConsumer.commitAsync();
                    break;
                }

                ++retryTimes;
                this.interruptSafeSleep((long)this.config.getRetryBackMs());
            }
        } catch (Exception var5) {
            log.error("listener exception, ignore record {}", record, var5);
        }

    }

    private void interruptSafeSleep(long timeoutMs) {
        long begin = System.currentTimeMillis();

        while(this.state != State.CLOSED) {
            long curr = System.currentTimeMillis();
            long rest = timeoutMs - (curr - begin);
            if (rest > 0L) {
                try {
                    TimeUnit.MICROSECONDS.sleep(rest);
                } catch (InterruptedException var10) {
                    log.warn("sleep is interrupted", var10);
                }
            }
        }

    }

    class BatchMessageListenerRunner implements Runnable {
        private final BatchMessageListener batchMessageListener;

        BatchMessageListenerRunner(BatchMessageListener batchMessageListener) {
            this.batchMessageListener = batchMessageListener;
        }

        @Override
        public void run() {
            ArrayList lastBatch = Lists.newArrayList();

            try {
                while(DefaultConsumer.this.state != State.CLOSED) {
                    List<KafkaMessage> recv = this.pollBatch(lastBatch);
                    if (CollectionUtils.isNotEmpty(recv)) {
                        List<List<KafkaMessage>> batches = Lists.partition(recv, DefaultConsumer.this.config.getBatchSize());
                        Iterator var4 = batches.iterator();

                        while(var4.hasNext()) {
                            List<KafkaMessage> batch = (List)var4.next();
                            DefaultConsumer.this.consumeRecord(batch, () -> {
                                return this.batchMessageListener.consume(batch);
                            });
                        }
                    }
                }
            } catch (WakeupException var9) {
            } finally {
                DefaultConsumer.this.kafkaConsumer.commitSync();
                DefaultConsumer.this.kafkaConsumer.close();
                if (DefaultConsumer.this.thread != null) {
                    DefaultConsumer.this.thread.interrupt();
                }

                DefaultConsumer.log.info("consumer {} stop", DefaultConsumer.this.consumerKey);
            }

        }

        private List<KafkaMessage> pollBatch(List<KafkaMessage> lastBatch) {
            long batchRecvTimeoutMs = (long)DefaultConsumer.this.config.getBatchRecvTimeoutMs();
            int batchSize = DefaultConsumer.this.config.getBatchSize();
            long end = System.currentTimeMillis() + batchRecvTimeoutMs;
            List<KafkaMessage> result = Lists.newArrayList(lastBatch);
            ConsumerRecords<String, String> records = DefaultConsumer.this.kafkaConsumer.poll(1000L);
            result.addAll(this.convert(records));
            long rest = 0L;

            while(DefaultConsumer.this.state != State.CLOSED && (rest = end - System.currentTimeMillis()) > 0L && result.size() < batchSize) {
                records = DefaultConsumer.this.kafkaConsumer.poll(rest);
                result.addAll(this.convert(records));
            }

            return result;
        }

        private List<KafkaMessage> convert(ConsumerRecords<String, String> records) {
            if (records != null && !records.isEmpty()) {
                List<KafkaMessage> result = Lists.newArrayList();
                Iterator var3 = records.iterator();

                while(var3.hasNext()) {
                    ConsumerRecord<String, String> record = (ConsumerRecord)var3.next();
                    result.add(new KafkaMessage(record));
                }

                return result;
            } else {
                return Lists.newArrayList();
            }
        }
    }

    class MessageListenerRunner implements Runnable {
        private final MessageListener listener;

        public MessageListenerRunner(MessageListener listener) {
            this.listener = listener;
        }

        public void run() {
            try {
                label87:
                while(true) {
                    if (DefaultConsumer.this.state != State.CLOSED) {
                        ConsumerRecords<String, String> records = DefaultConsumer.this.kafkaConsumer.poll(1000L);
                        if (records == null || records.isEmpty()) {
                            continue;
                        }

                        Iterator var2 = records.iterator();

                        while(true) {
                            if (!var2.hasNext()) {
                                continue label87;
                            }

                            ConsumerRecord<String, String> record = (ConsumerRecord)var2.next();
                            this.consumeSingleRecord(new KafkaMessage(record));
                        }
                    }
                }
            } catch (WakeupException var7) {
                var7.printStackTrace();
            } finally {
                DefaultConsumer.this.kafkaConsumer.commitSync();
                DefaultConsumer.this.kafkaConsumer.close();
                if (DefaultConsumer.this.thread != null) {
                    DefaultConsumer.this.thread.interrupt();
                }
                DefaultConsumer.log.info("consumer {} stop", DefaultConsumer.this.consumerKey);
            }
        }

        private void consumeSingleRecord(KafkaMessage kafkaMessage) {
            DefaultConsumer.this.consumeRecord(kafkaMessage, () -> {
                return this.listener.consume(kafkaMessage);
            });
        }
    }

    private static enum State {
        INIT,
        REGISTERED,
        CLOSED;

        private State() {
        }
    }
}
