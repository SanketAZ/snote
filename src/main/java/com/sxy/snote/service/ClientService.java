package com.sxy.snote.service;

import com.sxy.snote.dto.ClientDTO;
import com.sxy.snote.dto.ClientTokenDTO;
import com.sxy.snote.dto.ClientUpdateDTO;
import com.sxy.snote.model.Client;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.validation.BindingResult;

import java.util.UUID;

public interface ClientService {
    Client createClient(Client client, BindingResult bindingResult);
    Client updateClient(ClientUpdateDTO client);
    ClientDTO getClient(UUID clientId);
}
