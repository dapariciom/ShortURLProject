package com.example.shorturl.utils.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UrlNotFoundException extends RuntimeException{

    public  UrlNotFoundException(String message){
        super(message);
    }
}
