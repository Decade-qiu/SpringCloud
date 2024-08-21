package com.hmall.search.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@ConfigurationProperties(prefix = "hm.elasticsearch")
public class ElasticProperties {
    private String server;
}
