package com.ittouch.vectorsearchdemo.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticsearchConfig {
    @Value("${elasticsearch.url}")
    private String url;

    @Bean
    public ElasticsearchClient client() {
        return new ElasticsearchClient(restClientTransport());
    }

    @Bean
    public RestClientTransport restClientTransport() {
        var restClient = RestClient
                .builder(HttpHost.create(url))
                .build();

        return new RestClientTransport(
                restClient, new JacksonJsonpMapper());
    }
}
