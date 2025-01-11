package com.sxy.snote.configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import java.time.Duration;


@Configuration
@EnableElasticsearchRepositories(basePackages = "com.sxy.snote.elasticSearch.repo")
public class ElasticsearchConfig extends ElasticsearchConfiguration {

    @Override
    public ClientConfiguration clientConfiguration() {

        return ClientConfiguration.builder()
                .connectedToLocalhost()
                .withConnectTimeout(30000)
                .withSocketTimeout(Duration.ofMinutes(1))
                .build();
    }
}