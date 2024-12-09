package com.sxy.snote.exception.handler;

import com.sxy.snote.exception.*;
import com.sxy.snote.model.ErrorDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@RestControllerAdvice
public class ImageExceptionHandler {
    @ExceptionHandler(ImageUploadException.class)
    public ResponseEntity<ErrorDetails> imageNotUploaded(ImageUploadException exception, WebRequest request)
    {
        ErrorDetails error=new ErrorDetails(new Date(), exception.getMessage(), request.getDescription(false));
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    @ExceptionHandler(ImageGetException.class)
    public ResponseEntity<ErrorDetails> imageNotFound(ImageUploadException exception, WebRequest request)
    {
        ErrorDetails error=new ErrorDetails(new Date(), exception.getMessage(), request.getDescription(false));
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);
    }

    @ExceptionHandler(NotFileException.class)
    public ResponseEntity<ErrorDetails> notFileException(NotFileException exception, WebRequest request)
    {
        ErrorDetails error=new ErrorDetails(new Date(), exception.getMessage(), request.getDescription(false));
        return ResponseEntity
                .status(HttpStatus.NOT_ACCEPTABLE)
                .body(error);
    }

    @ExceptionHandler(FileDeletionException.class)
    public ResponseEntity<ErrorDetails> fileDeletion(FileDeletionException exception, WebRequest request)
    {
        ErrorDetails error=new ErrorDetails(new Date(), exception.getMessage(), request.getDescription(false));
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(error);
    }

    @ExceptionHandler(ImageUnauthorizedException.class)
    public ResponseEntity<ErrorDetails> imageUnauthorizedAccess(ImageUnauthorizedException exception, WebRequest request)
    {
        ErrorDetails error=new ErrorDetails(new Date(), exception.getMessage(), request.getDescription(false));
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(error);
    }

    @ExceptionHandler(TooManyImagesException.class)
    public ResponseEntity<ErrorDetails> manyImagesUploaded(TooManyImagesException exception,WebRequest request)
    {
        ErrorDetails error=new ErrorDetails(new Date(), exception.getMessage(), request.getDescription(false));
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    @ExceptionHandler(NoImagesUploadedException.class)
    public ResponseEntity<ErrorDetails> noImagesUploaded(NoImagesUploadedException exception,WebRequest request)
    {
        ErrorDetails error=new ErrorDetails(new Date(), exception.getMessage(), request.getDescription(false));
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

}
