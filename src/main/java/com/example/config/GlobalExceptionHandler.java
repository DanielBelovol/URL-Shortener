package com.example.config;

import com.example.exceptions.UrlExpiredException;
import com.example.exceptions.UrlNotFoundException;
import com.example.exceptions.UserNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = {UrlExpiredException.class})
    public ResponseEntity<Map<String, List<String>>> urlIsExpiredException(UrlExpiredException ex) {
        return getErrorsMap(ex, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(value = {UrlNotFoundException.class})
    public ResponseEntity<Map<String, List<String>>> urlNotFoundException(UrlNotFoundException ex) {
        return getErrorsMap(ex, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(value = {UserNotFoundException.class})
    public ResponseEntity<Map<String, List<String>>> userNotFoundException(UserNotFoundException ex) {
        return getErrorsMap(ex, HttpStatus.NOT_FOUND);
    }

    private ResponseEntity<Map<String, List<String>>> getErrorsMap(Throwable ex, HttpStatus status) {
        Map<String, List<String>> map = new HashMap<>();
        map.put("errors", Collections.singletonList(ex.getMessage()));
        return new ResponseEntity<>(map, new HttpHeaders(), status);
    }
}
