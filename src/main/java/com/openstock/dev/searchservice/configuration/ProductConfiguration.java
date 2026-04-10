package com.openstock.dev.searchservice.configuration;


import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProductConfiguration {
    @Value("${elastic.server_url}")
    String serverUrl;
    @Value("${elastic.server_port}")
    Integer serverPort;
    @Value("${elastic.api_key}")
    String apiKey;
    @Value("${elastic.connection_scheme}")
    String connectionScheme;

    @Bean
    public ElasticsearchClient elasticsearchClient() throws ElasticsearchException {
        // Setup the low-level client
        RestClient restClient = RestClient.builder(
                        new HttpHost(serverUrl, serverPort, connectionScheme))
                .setDefaultHeaders(
                        apiKey.isEmpty() ? new Header[0] : new Header[]{new BasicHeader("Authorization",
                                "ApiKey " + apiKey)}
                )
                .build();

        // Create the transport layer
        ElasticsearchTransport transport = new RestClientTransport(
                restClient, new JacksonJsonpMapper());

        // Return the Elasticsearch client
        return new ElasticsearchClient(transport);
    }
}

