package com.sxy.snote.exception;

public class NoImagesUploadedException extends RuntimeException{
    public NoImagesUploadedException(String msg)
    {
        super(msg);
    }
}
