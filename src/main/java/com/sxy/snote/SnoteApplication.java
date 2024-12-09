package com.sxy.snote;

import com.sxy.snote.configuration.RSAKeyRecord;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
public class SnoteApplication {

    public static void main(String[] args) {
        SpringApplication.run(SnoteApplication.class, args);
    }

}
