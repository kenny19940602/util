package com.kenny.util.nacos;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Map;

@Configuration
public class NacosConfigurations implements ImportAware {
    private Map<String, Object> annotationAttributes;
    @Value("${spring.cloud.nacos.config.server-addr}")
    private String nacosServer;
    @Autowired
    private ApplicationContext applicationContext;

    public NacosConfigurations() {
    }

    @Bean
    public NacosConfigFileClient nacosConfigFileClient() {
        return new NacosConfigFileClient(this.getNamespace(), this.getGroup(), this.nacosServer);
    }

    @Bean
    public NacosConfigClient nacosConfigClient() {
        return new NacosConfigClient(this.getDataId(), this.nacosConfigFileClient());
    }

    @Override
    public void setImportMetadata(AnnotationMetadata annotationMetadata) {
        this.annotationAttributes = annotationMetadata.getAnnotationAttributes("com.kenny.util.nacos.EnableNacosConfigClient");
    }

    public String getDataId() {
        return (String)this.annotationAttributes.get("dataId");
    }

    public String getGroup() {
        String group = this.applicationContext.getEnvironment().getProperty("spring.cloud.nacos.config.group");
        return StringUtils.isEmpty(group) ? "DEFAULT_GROUP" : group;
    }

    public String getNamespace() {
        return this.applicationContext.getEnvironment().getProperty("spring.cloud.nacos.config.namespace");
    }
}
