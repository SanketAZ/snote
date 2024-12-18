package com.sxy.snote.controller;

import com.sxy.snote.dto.ClientToken;
import com.sxy.snote.dto.RefreshTokenRequest;
import com.sxy.snote.model.Client;
import com.sxy.snote.service.ClientService;
import com.sxy.snote.service.impl.KeycloakAuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private KeycloakAuthService keycloakAuthService;

    @PostMapping("/login")
    public ClientToken loginUser(@RequestBody Client client,HttpServletResponse response){
        return keycloakAuthService.getClientToken(client);
    }

    @GetMapping("/token/refresh")
    public ResponseEntity<?> getAccessToken(@RequestBody RefreshTokenRequest refresh_token){
        return ResponseEntity.ok(keycloakAuthService.accessTokenFromRefresh(refresh_token));
    }

    @PostMapping("/register")
    public ClientToken registerUser(@Valid @RequestBody Client client,
                                    BindingResult bindingResult,
                                    HttpServletResponse httpServletResponse){
        System.out.println("sign-up");
        Client createdClient=clientService.createClient(client,bindingResult);
        return keycloakAuthService.getClientToken(createdClient);
    }
}
