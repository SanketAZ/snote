package com.sxy.snote.controller;


import com.sxy.snote.dto.ClientDTO;
import com.sxy.snote.dto.ClientUpdateDTO;
import com.sxy.snote.model.Client;
import com.sxy.snote.service.ClientService;
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

//    @PostMapping("/register")
//    public ResponseEntity<ClientTokenDTO> createClient(@RequestBody Client client)
//    {
//        ClientTokenDTO user=clientService.createClient(client);
//       return ResponseEntity
//               .status(HttpStatus.CREATED)
//               .body(user);


    @PutMapping("/{userId}")
    public ResponseEntity<Client> updateClient(@PathVariable("userId") UUID userId,@RequestBody ClientUpdateDTO client)
    {
        Client user=clientService.updateClient(client);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(user);
    }

    @PreAuthorize("hasAuthority('SCOPE_READ')")
    @GetMapping("/{userId}")
    public ResponseEntity<ClientDTO> getClient(@PathVariable("userId") UUID clientId,Authentication authentication)
    {
        ClientDTO user=clientService.getClient(clientId);
        System.out.println("Authorities: " + authentication.getAuthorities());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(user);
    }

//    @GetMapping("/protected")
//    public ResponseEntity<String> getProtectedData(Authentication authentication) {
//        System.out.println("Authorities: " + authentication.getAuthorities());
//        return ResponseEntity.ok("Protected data");
//    }

}