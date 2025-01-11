package com.sxy.snote.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@ConfigurationProperties(prefix = "jwt")
public record RSAKeyRecord(RSAPublicKey rsaPublicKey, RSAPrivateKey rsaPrivateKey) {
}