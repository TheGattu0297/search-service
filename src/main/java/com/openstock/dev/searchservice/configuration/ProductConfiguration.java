package com.openstock.dev.searchservice.configuration;


import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;

@Configuration
public class ProductConfiguration extends ElasticsearchConfiguration {
    @Value("${elastic.url}")
    String url;
    @Value("${elastic.port}")
    String port;
    @Override
    @NonNull
    public ClientConfiguration clientConfiguration() {
        return ClientConfiguration.builder()
                .connectedTo(url+":"+port).build();
    }
}

