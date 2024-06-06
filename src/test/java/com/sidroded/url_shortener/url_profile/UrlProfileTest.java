package com.sidroded.url_shortener.url_profile;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class UrlProfileTest {

    @Test
    public void testShortUrlGeneration() {
        UrlProfile urlProfile = new UrlProfile("http://example.com", 1L);
        String shortUrl = urlProfile.getShortUrl();

        assertNotNull(shortUrl);
        assertEquals(8, shortUrl.length());

        for (char c : shortUrl.toCharArray()) {
            assertTrue(Character.isLetterOrDigit(c) || c == '_');
        }
    }

}