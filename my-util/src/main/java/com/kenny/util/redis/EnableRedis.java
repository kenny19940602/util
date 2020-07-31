package com.kenny.util.redis;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({RedisConfiguration.class,RedisService.class})
public @interface EnableRedis {
}
