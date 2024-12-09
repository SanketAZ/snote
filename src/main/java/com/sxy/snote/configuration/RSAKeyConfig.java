package com.sxy.snote.configuration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(RSAKeyRecord.class)
public class RSAKeyConfig {

    private final RSAKeyRecord rsaKeyRecord;

    public RSAKeyConfig(RSAKeyRecord rsaKeyRecord) {
        this.rsaKeyRecord = rsaKeyRecord;
    }

    @Bean
    public RSAKeyRecord rsaKeyRecord() {
        return rsaKeyRecord;
    }
}