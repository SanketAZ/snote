package com.sxy.snote.exception;

public class TooManyImagesException extends RuntimeException{
    public TooManyImagesException(String msg)
    {
        super(msg);
    }
}
