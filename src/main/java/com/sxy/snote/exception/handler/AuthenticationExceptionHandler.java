package com.sxy.snote.exception.handler;

import com.sxy.snote.exception.AuthException;
import com.sxy.snote.model.ErrorDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@RestControllerAdvice
public class AuthenticationExceptionHandler {

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ErrorDetails> AuthException(AuthException exception, WebRequest request)
    {
        ErrorDetails error=new ErrorDetails(new Date(), exception.getMessage(), request.getDescription(false));
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(error);
    }

}
