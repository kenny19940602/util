package com.kenny.util.nacos;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Import({NacosConfigurations.class})
public @interface EnableNacosConfigClient {
    String dataId();
}
