package com.sxy.snote.service.impl;

import com.sxy.snote.dto.*;
import com.sxy.snote.exception.KeyclockException;
import com.sxy.snote.helper.MapperService;
import com.sxy.snote.model.Client;
import jakarta.ws.rs.core.Response;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class KeycloakAuthService {

    private final WebClient webClient = WebClient.create();

    private final String keycloakTokenUrl="http://localhost:8081/realms/myrealm/protocol/openid-connect/token";

    public ClientToken getClientToken(Client client) {
        TokenResponseDTO tokenResponseDTO=generateTokenOnSignUp(client);
        return MapperService.getClientToken(client,tokenResponseDTO);
    }

    public TokenResponseDTO generateAccessToken(TokenRequest tokenRequest) {
        // Prepare body
        Map<String, String> body = Map.of(
                "grant_type", "authorization_code",
                "client_id", "practice_login",
                "client_secret", "aNrvYaEW5cMFm8FaLZn4KSZWfF3MfzzH",
                "code", tokenRequest.getCode(),
                "redirect_uri", tokenRequest.getRedirectUri()
        );

        return webClient.post()
                .uri(keycloakTokenUrl)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue(toUrlEncodedString(body)) // Encode as URL-encoded string
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        clientResponse -> clientResponse.bodyToMono(String.class)
                                .map(errorBody -> {
                                    // Build a JAX-RS Response
                                    Response errorResponse = Response
                                            .status(clientResponse.statusCode().value()) // Use the HTTP status from the response
                                            .entity(errorBody) // Include error body
                                            .build();
                                    throw new KeyclockException("Error from Keycloak: ", errorResponse);
                                })
                )
                .bodyToMono(TokenResponseDTO.class) // Map response to TokenDTO
                .block(); // Blocking for demonstration, use reactive programming in production
    }

    public TokenResponseDTO generateTokenOnSignUp(Client client) {
        // Prepare body
        Map<String, String> body = Map.of(
                "grant_type", "password",
                "client_id", "snote-application",
                "client_secret", "zr8j8tiPwh6kQK4vYchVbZXEbjsOpjld",
                "username", client.getUsername(),
                "password", client.getPassword()
        );

        return webClient.post()
                .uri(keycloakTokenUrl)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue(toUrlEncodedString(body)) // Encode as URL-encoded string
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        clientResponse -> clientResponse.bodyToMono(String.class)
                                .map(errorBody -> {
                                    // Build a JAX-RS Response
                                    Response errorResponse = Response
                                            .status(clientResponse.statusCode().value()) // Use the HTTP status from the response
                                            .entity(errorBody) // Include error body
                                            .build();
                                    throw new KeyclockException("Error from Keycloak: ", errorResponse);
                                })
                )
                .bodyToMono(TokenResponseDTO.class) // Map response to TokenDTO
                .block(); // Blocking for demonstration, use reactive programming in production
    }

    public TokenResponseDTO accessTokenFromRefresh(RefreshTokenRequest refresh_token) {
        // Prepare body
        Map<String, String> body = Map.of(
                "grant_type", "refresh_token",
                "client_id", "snote-application",
                "client_secret", "zr8j8tiPwh6kQK4vYchVbZXEbjsOpjld",
                "refresh_token", refresh_token.getRefresh_token());

        return webClient.post()
                .uri(keycloakTokenUrl)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue(toUrlEncodedString(body)) // Encode as URL-encoded string
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        clientResponse -> clientResponse.bodyToMono(String.class)
                                .map(errorBody -> {
                                    // Build a JAX-RS Response
                                    Response errorResponse = Response
                                            .status(clientResponse.statusCode().value()) // Use the HTTP status from the response
                                            .entity(errorBody) // Include error body
                                            .build();
                                    throw new KeyclockException("Error from Keycloak: ", errorResponse);
                                })
                )
                .bodyToMono(TokenResponseDTO.class) // Map response to TokenDTO
                .block(); // Blocking for demonstration, use reactive programming in production
    }

    private String toUrlEncodedString(Map<String, String> params) {
        return params.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .reduce((a, b) -> a + "&" + b)
                .orElse("");
    }
}
