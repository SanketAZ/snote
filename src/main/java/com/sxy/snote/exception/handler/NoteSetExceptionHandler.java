package com.sxy.snote.exception.handler;

import com.sxy.snote.exception.NoteSetException;
import com.sxy.snote.exception.ResourceNotFoundException;
import com.sxy.snote.model.ErrorDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@RestControllerAdvice
public class NoteSetExceptionHandler {
    @ExceptionHandler(NoteSetException.class)
    public ResponseEntity<ErrorDetails> userNotFound(NoteSetException exception, WebRequest request)
    {
        ErrorDetails error=new ErrorDetails(new Date(), exception.getMessage(), request.getDescription(false));
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);
    }
}
