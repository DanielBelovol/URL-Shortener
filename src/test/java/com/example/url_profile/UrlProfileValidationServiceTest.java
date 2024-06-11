package com.example.url_profile;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class UrlProfileValidationServiceTest {
    @Autowired
    private UrlProfileValidationService urlProfileValidationService;
    @Test
    void isValidURLTest(){
        String validUrl = "https://www.youtube.com";

        boolean result = urlProfileValidationService.isValidURL(validUrl);
        assertTrue(result);
    }
    @Test
    void isNotValidURLTest(){
        String invalidUrl = "htp://invalid-url";

        boolean result = urlProfileValidationService.isValidURL(invalidUrl);
        assertFalse(result);
    }
}