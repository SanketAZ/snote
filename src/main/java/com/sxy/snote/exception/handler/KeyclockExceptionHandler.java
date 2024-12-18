package com.sxy.snote.exception.handler;

import com.sxy.snote.exception.ImageUploadException;
import com.sxy.snote.exception.KeyclockException;
import com.sxy.snote.model.ErrorDetails;
import jakarta.ws.rs.core.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@RestControllerAdvice
public class KeyclockExceptionHandler {
    @ExceptionHandler(KeyclockException.class)
    public ResponseEntity<?> keyclockException(KeyclockException exception)
    {

        Response keycloakResponse=exception.getKeycloakResponse();
        int status=keycloakResponse.getStatus();
        String errorMessage=keycloakResponse.readEntity(String.class);

        ErrorDetails error=new ErrorDetails(new Date(), exception.getMessage(), errorMessage);

        // Map Keycloak response status to Spring's HttpStatus
        HttpStatus httpStatus = HttpStatus.resolve(status) != null ? HttpStatus.valueOf(status) : HttpStatus.INTERNAL_SERVER_ERROR;

        return ResponseEntity
                .status(httpStatus)
                .body(error);
       }
}
