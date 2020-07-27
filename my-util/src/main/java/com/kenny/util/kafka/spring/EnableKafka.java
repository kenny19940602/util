package com.kenny.util.kafka.spring;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Import({KafkaBootstrap.class})
public @interface EnableKafka {
}
