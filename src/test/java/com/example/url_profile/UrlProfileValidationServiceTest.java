package com.example.url_profile;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

public class UrlProfileValidationServiceTest {

    @InjectMocks
    private UrlProfileValidationService urlProfileValidationService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testValidURL() {
        String validUrl = "http://www.example.com";
        assertTrue(urlProfileValidationService.isValidURL(validUrl));
    }

    @Test
    public void testInvalidURL() {
        String invalidUrl = "http://invalid.url";
        assertFalse(urlProfileValidationService.isValidURL(invalidUrl));
    }

    @Test
    public void testMalformedURL() {
        String malformedUrl = "invalid-url";
        assertFalse(urlProfileValidationService.isValidURL(malformedUrl));
    }

    @Test
    public void testNonExistentURL() {
        String nonExistentUrl = "http://www.thisurldoesnotexist.com";
        assertFalse(urlProfileValidationService.isValidURL(nonExistentUrl));
    }

    @Test
    public void testURLWithTimeout() {
        String urlWithTimeout = "http://10.255.255.1"; // IP address to simulate timeout
        assertFalse(urlProfileValidationService.isValidURL(urlWithTimeout));
    }
}