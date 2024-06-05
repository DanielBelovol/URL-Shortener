package com.example.exceptions;

public class UrlExpiredException extends Exception{
    public UrlExpiredException() {
        super("This url is expired");
    }
}
