package com.example.url_profile;

import java.security.SecureRandom;
import java.util.Random;

public class UrlProfileUtil {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public static String generateShortUrl(int length) {
        Random random = new SecureRandom();
        StringBuilder shortUrl = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            shortUrl.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return shortUrl.toString();
    }
}
