package com.sxy.snote.controller;


import com.sxy.snote.dto.ClientDTO;
import com.sxy.snote.dto.ClientUpdateDTO;
import com.sxy.snote.dto.TokenResponseDTO;
import com.sxy.snote.helper.MapperService;
import com.sxy.snote.model.Client;
import com.sxy.snote.service.ClientService;
import com.sxy.snote.service.impl.KeycloakAuthService;
import com.sxy.snote.service.impl.KeycloakService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private KeycloakService keycloakService;
    @Autowired
    private KeycloakAuthService keycloakAuthService;

    @PutMapping("/{userId}")
    public ResponseEntity<ClientDTO> updateClient(@PathVariable("userId") UUID userId,@RequestBody ClientUpdateDTO client) {
        Client user=clientService.updateClient(client);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(MapperService.getClientDTO(user));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteClient(@PathVariable("userId") UUID userId) {
        clientService.deleteClient(userId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT).build();
    }

    @PreAuthorize("hasAuthority('ROLE_USER_READ')")
    @GetMapping("/{userId}")
    public ResponseEntity<ClientDTO> getClient(@PathVariable("userId") UUID clientId,Authentication authentication)
    {
        ClientDTO user=clientService.getClient(clientId);
        System.out.println("Authorities: " + authentication.getAuthorities());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(user);
    }
}