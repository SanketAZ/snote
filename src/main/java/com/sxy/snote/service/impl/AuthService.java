//package com.sxy.snote.service.impl;
//
//import com.sxy.snote.dto.ClientTokenDTO;
//import com.sxy.snote.exception.AuthException;
//import com.sxy.snote.helper.MapperService;
//import com.sxy.snote.model.Client;
//import com.sxy.snote.model.RefreshTokenEntity;
//import com.sxy.snote.repository.ClientRepo;
//import com.sxy.snote.repository.RefreshTokenRepo;
//import jakarta.servlet.http.Cookie;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.stereotype.Service;
//import org.springframework.web.server.ResponseStatusException;
//
//import java.util.Optional;
//
//@Service
//public class AuthService {
//    @Autowired
//    private RefreshTokenRepo refreshTokenRepo;
//
//    @Autowired
//    private JWTService jwtService;
//
//    @Autowired
//    private ClientRepo clientRepo;
//
//    @Autowired
//    private AuthenticationManager authenticationManager;
//
//    public Object getAccessTokenUsingRefreshToken(String authorizationHeader)
//    {
//        if(!authorizationHeader.startsWith("Bearer"))
//        {
//            return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Please verify your token type");
//        }
//
//        final String refreshToken=authorizationHeader.substring(7);
//
//        //Find refreshToken from database and should not be revoked : Same thing can be done through filter.
//        var refreshTokenEntity = refreshTokenRepo.findByRefreshToken(refreshToken)
//                .filter(tokens-> !tokens.isRevoked())
//                .orElseThrow(()-> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Refresh token revoked"));
//
//        String token=jwtService.genrateToken(refreshTokenEntity.getUser());
//
//        return MapperService.getClientTokenDTO(refreshTokenEntity.getUser(), token);
//
//    }
//
//    public ClientTokenDTO verify(Client client, HttpServletResponse response) {
//        try {
//            Authentication authentication = authenticationManager
//                    .authenticate(new UsernamePasswordAuthenticationToken(client.getUsername(), client.getPassword()));
//            if (authentication.isAuthenticated()){
//                Optional<Client> optional = clientRepo.findByUsername(client.getUsername());
//                String token = jwtService.genrateToken(optional.get());
//                String refreshToken = jwtService.genrateRefreshToken(optional.get());
//                saveRefreshToken(optional.get(),refreshToken);
//                createRefreshTokenCookie(response,refreshToken);
//
//                return MapperService.getClientTokenDTO(optional.get(), token);
//            }
//        }catch (AuthenticationException e) {
//            throw new AuthException("Authentication failed: " + e.getMessage());
//        }
//        return null;
//    }
//
//    public ClientTokenDTO signUpToken(Client client, HttpServletResponse response) {
//        String token = jwtService.genrateToken(client);
//        String refreshToken = jwtService.genrateRefreshToken(client);
//        saveRefreshToken(client,refreshToken);
//        createRefreshTokenCookie(response,refreshToken);
//        return MapperService.getClientTokenDTO(client,token);
//    }
//
//
//
//    private void createRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
//        Cookie refreshTokenCookie=new Cookie("refresh_token",refreshToken);
//        refreshTokenCookie.setHttpOnly(true);
//        refreshTokenCookie.setSecure(true);
//        refreshTokenCookie.setMaxAge(15*24*60*60);
//        response.addCookie(refreshTokenCookie);
//    }
//
//    private void saveRefreshToken(Client client,String refreshToken)
//    {
//        RefreshTokenEntity tokenEntity=
//                RefreshTokenEntity.builder()
//                        .refreshToken(refreshToken)
//                        .revoked(false)
//                        .user(client)
//                        .build();
//        refreshTokenRepo.save(tokenEntity);
//    }
//}
