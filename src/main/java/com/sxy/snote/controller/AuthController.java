package com.sxy.snote.controller;

import com.sxy.snote.dto.ClientDTO;
import com.sxy.snote.dto.ClientTokenDTO;
import com.sxy.snote.model.Client;
import com.sxy.snote.service.ClientService;
import com.sxy.snote.service.impl.AuthService;
import com.sxy.snote.service.impl.ClientServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private AuthService authService;

    @PostMapping("/sign-in")
    public ClientTokenDTO loginUser(@RequestBody Client client,HttpServletResponse response){
        return authService.verify(client,response);

    }

    @PreAuthorize("hasAuthority('SCOPE_REFRESH-TOKEN')")
    @GetMapping("/refresh-token")
    public ResponseEntity<?> getAccessToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader){
        return ResponseEntity.ok(authService.getAccessTokenUsingRefreshToken(authorizationHeader));
    }

    @PostMapping("/sign-up")
    public ClientTokenDTO registerUser(@Valid @RequestBody Client client,
                                          BindingResult bindingResult,
                                       HttpServletResponse httpServletResponse){
        System.out.println("sign-up");
        Client createdClient=clientService.createClient(client,bindingResult);
        return authService.signUpToken(createdClient,httpServletResponse);
    }
}
