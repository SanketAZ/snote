package com.sxy.snote.exception;

import jakarta.ws.rs.core.Response;

public class KeyclockException extends RuntimeException{

    private final Response keycloakResponse;

    public  KeyclockException(String msg, Response response)
    {
        super(msg);
        this.keycloakResponse = response;
    }

    public Response getKeycloakResponse() {
        return keycloakResponse;
    }
}
