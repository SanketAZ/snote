package com.sxy.snote.service.impl;

import com.sxy.snote.repository.RefreshTokenRepo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
public class LogoutHandlerService implements LogoutHandler {

    @Autowired
    private RefreshTokenRepo refreshTokenRepo;
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

        final String autHeader= request.getHeader(HttpHeaders.AUTHORIZATION);

        if(!autHeader.startsWith("Bearer")) {
            return;
        }
        String refreshToken=autHeader.substring(7);

        var storedRefreshToken=refreshTokenRepo.findByRefreshToken(refreshToken)
                .map(token->{
                    token.setRevoked(true);
                    return  refreshTokenRepo.save(token);
                })
                .orElse(null);
    }
}
