package com.sxy.snote.service.impl;

import com.sxy.snote.dto.ClientDTO;
import com.sxy.snote.dto.ClientUpdateDTO;
import com.sxy.snote.exception.UserNotFoundException;
import com.sxy.snote.helper.MapperService;
import com.sxy.snote.model.Client;
import com.sxy.snote.repository.ClientRepo;
import com.sxy.snote.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.Optional;
import java.util.UUID;

@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    private ClientRepo clientRepo;

    @Autowired
    private KeycloakService keycloakService;


    public Client createClient(Client client, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return null;
        }
        String keycloakId=keycloakService.createUserInKeycloak(client);
        if(keycloakId==null)
            throw new IllegalArgumentException("keycloakId is null!!!");
        client.setKeycloakId(keycloakId);
        clientRepo.save(client);
        return clientRepo.save(client);
    }

    public Client updateClient(ClientUpdateDTO client) {
        Optional<Client> user=clientRepo.findById(client.getId());
        if(user.isEmpty()) {
            throw new UserNotFoundException("User does not exists");
        }
        client.setKeycloakId(user.get().getKeycloakId());
        keycloakService.updateUserInKeycloak(client);
        user.get().setEmail(client.getEmail());
        return clientRepo.save(user.get());
    }

    @Override
    public ClientDTO getClient(UUID clientId) {
        Optional<Client> client=clientRepo.findById(clientId);

        if(client.isEmpty())
            throw new UserNotFoundException("User not found");

        return MapperService.getClientDTO(client.get());
    }

    @Override
    public void deleteClient(UUID clientId) {
        Optional<Client> user=clientRepo.findById(clientId);
        if(user.isEmpty()) {
            throw new UserNotFoundException("User does not exists");
        }
        keycloakService.deleteUserInKeycloak(user.get().getKeycloakId());
        clientRepo.delete(user.get());
    }
}
