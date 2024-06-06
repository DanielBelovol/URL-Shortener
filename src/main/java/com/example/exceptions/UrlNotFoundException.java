package com.example.exceptions;

public class UrlNotFoundException extends Exception{
    public UrlNotFoundException(long id){
        super("Url with id#" + id + " not found");
    }
}
