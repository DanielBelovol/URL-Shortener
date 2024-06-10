package com.example.url_profile;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UrlProfileUtilTest {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    @Test
    void testGenerateShortUrlLength() {
        int length = 8;
        String shortUrl = UrlProfileUtil.generateShortUrl(length);
        assertNotNull(shortUrl);
        assertEquals(length, shortUrl.length());
    }

    @Test
    void testGenerateShortUrlCharacters() {
        int length = 10;
        String shortUrl = UrlProfileUtil.generateShortUrl(length);
        assertNotNull(shortUrl);

        for (char c : shortUrl.toCharArray()) {
            assertTrue(CHARACTERS.indexOf(c) >= 0, "Character " + c + " is not valid");
        }
    }

    @Test
    void testGenerateShortUrlRandomness() {
        int length = 8;
        String shortUrl1 = UrlProfileUtil.generateShortUrl(length);
        String shortUrl2 = UrlProfileUtil.generateShortUrl(length);
        assertNotEquals(shortUrl1, shortUrl2);
    }

    @Test
    void testGenerateShortUrlWithZeroLength() {
        int length = 0;
        String shortUrl = UrlProfileUtil.generateShortUrl(length);
        assertNotNull(shortUrl);
        assertEquals(length, shortUrl.length());
    }

    @Test
    void testGenerateShortUrlWithNegativeLength() {
        int length = -1;
        assertThrows(NegativeArraySizeException.class, () -> {
            UrlProfileUtil.generateShortUrl(length);
        });
    }
}