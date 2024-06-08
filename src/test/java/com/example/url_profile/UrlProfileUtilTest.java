package com.example.url_profile;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UrlProfileUtilTest {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    @Test
    public void testGenerateShortUrl_Length6() {
        String shortUrl = UrlProfileUtil.generateShortUrl(6);
        assertNotNull(shortUrl);
        assertEquals(6, shortUrl.length());
    }

    @Test
    public void testGenerateShortUrl_Length10() {
        String shortUrl = UrlProfileUtil.generateShortUrl(10);
        assertNotNull(shortUrl);
        assertEquals(10, shortUrl.length());
    }

    @Test
    public void testGenerateShortUrl_ContainsValidCharacters() {
        String shortUrl = UrlProfileUtil.generateShortUrl(10);
        assertNotNull(shortUrl);
        for (char c : shortUrl.toCharArray()) {
            assertTrue(CHARACTERS.contains(String.valueOf(c)), "Invalid character found: " + c);
        }
    }

    @Test
    public void testGenerateShortUrl_UniqueValues() {
        String shortUrl1 = UrlProfileUtil.generateShortUrl(10);
        String shortUrl2 = UrlProfileUtil.generateShortUrl(10);
        assertNotEquals(shortUrl1, shortUrl2, "Generated URLs should be unique");
    }

    @Test
    public void testGenerateShortUrl_Randomness() {
        int length = 8;
        boolean hasUpperCase = false;
        boolean hasLowerCase = false;
        boolean hasDigit = false;
        String shortUrl = UrlProfileUtil.generateShortUrl(length);

        for (char c : shortUrl.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasUpperCase = true;
            } else if (Character.isLowerCase(c)) {
                hasLowerCase = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            }
        }

        assertTrue(hasUpperCase, "URL should contain at least one uppercase letter");
        assertTrue(hasLowerCase, "URL should contain at least one lowercase letter");
        assertTrue(hasDigit, "URL should contain at least one digit");
    }
}