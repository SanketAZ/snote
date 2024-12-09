package com.sxy.snote.service.impl;

import com.sxy.snote.dto.ClientDTO;
import com.sxy.snote.dto.ClientTokenDTO;
import com.sxy.snote.dto.ClientUpdateDTO;
import com.sxy.snote.exception.AuthException;
import com.sxy.snote.exception.UserExistException;
import com.sxy.snote.exception.UserNotFoundException;
import com.sxy.snote.helper.MapperService;
import com.sxy.snote.model.Client;
import com.sxy.snote.model.RefreshTokenEntity;
import com.sxy.snote.repository.ClientRepo;
import com.sxy.snote.repository.RefreshTokenRepo;
import com.sxy.snote.service.ClientService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.Optional;
import java.util.UUID;

@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    private ClientRepo clientRepo;

    @Autowired
    private RefreshTokenRepo refreshTokenRepo;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    private final BCryptPasswordEncoder bCryptPasswordEncoder=new BCryptPasswordEncoder(10);

    public Client createClient(Client client, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return null;
        }

        if(clientRepo.existsByEmail(client.getEmail()))
            throw new UserExistException("Email exists");

        if(clientRepo.existsByUsername(client.getUsername()))
            throw new UserExistException("Username Exists");


        client.setPassword(bCryptPasswordEncoder.encode(client.getPassword()));
        clientRepo.save(client);

        return clientRepo.save(client);
//        String token = jwtService.genrateToken(client);
//        String refreshToken = jwtService.genrateRefreshToken(client);
//
//        saveRefreshToken(client,refreshToken);
//        createRefreshTokenCookie(response,refreshToken);
//        return MapperService.getClientTokenDTO(client,token);
    }

    public Client updateClient(ClientUpdateDTO client)
    {
        Optional<Client> user=clientRepo.findById(client.getClientDTO().getId());
        if(user.isEmpty())
        {
            throw new UserNotFoundException("User does not exists");
        }

        Optional<Client> updatedUser=clientRepo.findByEmail(client.getClientDTO().getEmail());
        if(updatedUser.isPresent() && !client.getClientDTO().getEmail().equals(updatedUser.get().getEmail())) {
            throw new UserExistException("Email exists");
        }
        updatedUser=clientRepo.findByUsername(client.getClientDTO().getUsername());
        if(updatedUser.isPresent() && !client.getClientDTO().getUsername().equals(updatedUser.get().getUsername())) {
            throw new UserExistException("Username Exists");
        }
        client.setPassword(bCryptPasswordEncoder.encode(client.getPassword()));
        return clientRepo.save(MapperService.convertClientUpdateDTO_Client(client));
    }

    @Override
    public ClientDTO getClient(UUID clientId) {
        Optional<Client> client=clientRepo.findById(clientId);

        if(client.isEmpty())
            throw new UserNotFoundException("User not found");

        return MapperService.getClientDTO(client.get());
    }
}
