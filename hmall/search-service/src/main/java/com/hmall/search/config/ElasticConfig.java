package com.hmall.search.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.io.IOException;

@Configuration
@EnableConfigurationProperties(ElasticProperties.class)
public class ElasticConfig {

    private final RestHighLevelClient restHighLevelClient;

    public ElasticConfig(ElasticProperties properties) {
        this.restHighLevelClient = new RestHighLevelClient(RestClient.builder(
                HttpHost.create("http://" + properties.getServer())
        ));
    }

    @Bean
    public RestHighLevelClient restHighLevelClient() {
        return this.restHighLevelClient;
    }

    @PreDestroy
    public void onDestroy() throws IOException {
        if (restHighLevelClient != null) {
            this.restHighLevelClient.close();
        }
    }

}
