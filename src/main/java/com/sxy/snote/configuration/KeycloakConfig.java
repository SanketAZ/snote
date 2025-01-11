package com.sxy.snote.configuration;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;


@Configuration
@Profile("prod")
public class KeycloakConfig {
    @Value("${keycloak.auth-server-url}")
    private String authServerUrl;

    @Value(value = "${keycloak.realm}")
    private String realm;

    @Value("${keycloak.admin.username}")
    private String adminUsername;

    @Value("${keycloak.resource}")
    private String clientId;

    @Value("${keycloak.admin.password}")
    private String adminPassword;

    @Value("${keycloak.credentials.secret}")
    private String clientSecret;

    @Bean
    public Keycloak keycloak()
    {
        System.out.println(realm);
        return KeycloakBuilder.builder()
                .serverUrl(authServerUrl)
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .realm("master").build();
    }
}
