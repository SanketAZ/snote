package com.sxy.snote.service.impl;

import com.sxy.snote.dto.ClientUpdateDTO;
import com.sxy.snote.exception.KeyclockException;
import com.sxy.snote.model.Client;
import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.net.URI;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class KeycloakService {

    @Autowired
    private Keycloak keycloak;

    @Value("${keycloak.resource}")
    private String clientId;

    @Value("${client.roles}")
    private String keycloakAvailableRoles;

    //This method crete the user in keycloak and also assign the role
    public String createUserInKeycloak(Client client) {

        // Create UserRepresentation
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername(client.getUsername());
        userRepresentation.setEmail(client.getEmail());
        userRepresentation.setEnabled(true);

        //Set User Credentials
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(client.getPassword());
        credential.setTemporary(false);
        userRepresentation.setCredentials(Collections.singletonList(credential));

        //Set roles (currently it not working)
        //need to check
//        Map<String,List<String>>rolesForClient=getClientRolesMap(client.getRoles(),keycloakAvailableRoles);
//        userRepresentation.setClientRoles(rolesForClient);

        //Create User in Keycloak
        RealmResource realmResource = keycloak.realm("myrealm");
        UsersResource usersResource = realmResource.users();
        Response response = usersResource.create(userRepresentation);

        if(response.getStatus()!=201)
            throw new KeyclockException("Keycloak error occurred", response);

        //Setting the roles separability
        assignClientRoles(extractUserId(response),"snote-application",getRoles(client.getRoles()));
        return extractUserId(response);
    }

    public void updateUserInKeycloak(ClientUpdateDTO clientUpdateDTO) {
        UserResource userResource=keycloak.realm("myrealm").users().get(clientUpdateDTO.getKeycloakId());
        UserRepresentation userRepresentation=userResource.toRepresentation();

        userRepresentation.setEmail(clientUpdateDTO.getEmail());
        try {
            userResource.update(userRepresentation);
        }catch (ClientErrorException e) {
            throw new KeyclockException("Keycloak error occurred", e.getResponse());
        }
    }

    public void deleteUserInKeycloak(String keycloakId) {
        Response response =keycloak.realm("myrealm").users().delete(keycloakId);
        if(response.getStatus()!=204)
            throw new KeyclockException("Keycloak error occurred", response);
    }


    private void assignClientRoles(String userId, String clientId, List<String> roles) {
        RealmResource realmResource = keycloak.realm("myrealm");
        UserResource userResource = realmResource.users().get(userId);

        // Fetch Client UUID
        String clientUuid = realmResource.clients().findByClientId(clientId).get(0).getId();
        //String clientUuid = "8383b276-d6cb-4dac-ae67-e478e66ef387";

        //from provided roles we are just taking client roles which are present on keycloak
        List<String>presentClientRoles=getAvailableClientRoles(getRoles(keycloakAvailableRoles),roles);
        if(presentClientRoles.isEmpty())
            throw new IllegalArgumentException("Client roles provided are not present in keycloak!!!");

        // Fetch Client Roles
        List<RoleRepresentation> clientRoles = presentClientRoles.stream()
                .map(roleName -> realmResource.clients().get(clientUuid).roles().get(roleName).toRepresentation())
                .toList();
        // Assign Roles
        userResource.roles().clientLevel(clientUuid).add(clientRoles);
    }

    List<String> getRoles(String rolesStr)
    {
        return  Arrays.stream(rolesStr.split(" ")).distinct().toList();
    }

    List<String> getAvailableClientRoles(List<String>clientRoles,List<String>roles)
    {
        return  roles.stream()
                        .filter(clientRoles::contains)
                        .distinct()
                        .collect(Collectors.toList());

    }

    Map<String,List<String>> getClientRolesMap(String clientRoles,String roles) {
        //Set roles
        List<String>pram1=getRoles(roles);
        List<String>pram2=getRoles(clientRoles);
        List<String>rolesForClient=getAvailableClientRoles(pram1,pram2);
        Map<String, List<String>> clientRolesMap = new HashMap<>();
        clientRolesMap.put("8383b276-d6cb-4dac-ae67-e478e66ef387",rolesForClient);
        return clientRolesMap;
    }

    String extractUserId(Response response) {
        // Ensure the response is not null
        if (response == null) {
            throw new IllegalArgumentException("Response object cannot be null");
        }
        // Get the Location header
        URI location = response.getLocation();

        // Check if the Location header exists
        if (location != null) {
            String locationPath = location.getPath();
            // Extract the user ID from the last part of the path
            return locationPath.substring(locationPath.lastIndexOf('/') + 1);
        }
        // If Location header is missing, return null
        return null;
    }
}
