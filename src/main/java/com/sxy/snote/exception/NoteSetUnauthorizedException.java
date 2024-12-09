package com.sxy.snote.exception;

public class NoteSetUnauthorizedException extends RuntimeException{
    public NoteSetUnauthorizedException(String msg){
        super(msg);
    }
}
