package com.example.url_profile;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class UrlProfileValidationService {
    public boolean isValidURL(String url) {
        try {
            URL urlObj = new URL(url);
            HttpURLConnection huc = (HttpURLConnection) urlObj.openConnection();
            huc.setRequestMethod("HEAD");
            huc.setConnectTimeout(3000);
            huc.setReadTimeout(3000);
            int responseCode = huc.getResponseCode();
            return (200 <= responseCode && responseCode <= 399);
        } catch (IOException e) {
            return false;
        }
    }
}
