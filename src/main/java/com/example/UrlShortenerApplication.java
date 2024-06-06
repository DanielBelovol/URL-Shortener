package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UrlShortenerApplication {

    public static void main(String[] args) {
        //SpringApplication.run(UrlShortenerApplication.class, args);
        SpringApplication app = new SpringApplication(UrlShortenerApplication.class);
        app.setAdditionalProfiles("default"); // write here the name of profile (default / prod)
        app.run(args);
    }

}
