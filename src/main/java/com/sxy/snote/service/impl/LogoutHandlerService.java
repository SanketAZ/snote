package com.sxy.snote.service.impl;

import com.sxy.snote.exception.KeyclockException;
import com.sxy.snote.repository.RefreshTokenRepo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.ClientErrorException;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
public class LogoutHandlerService implements LogoutHandler {

    @Autowired
    private RefreshTokenRepo refreshTokenRepo;

    private final Keycloak keycloak;
    private final JwtDecoder jwtDecoder;

    public LogoutHandlerService(Keycloak keyclocak,JwtDecoder jwtDecoder){
        this.keycloak=keyclocak;
        this.jwtDecoder=jwtDecoder;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() != null) {
            String userId = authentication.getName();
            // Invoke Keycloak logout
            try {
                RealmResource realmResource = keycloak.realm("myrealm");
                realmResource.users().get(userId).logout();
            } catch (ClientErrorException e) {
                throw new KeyclockException("Keycloak error occurred", e.getResponse());
            }
        }

    }
}
