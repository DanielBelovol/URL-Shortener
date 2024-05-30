package com.sidroded.url_shortener.url_profile;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Random;

@Data
@Entity
public class UrlProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String fullUrl;
    @Column
    private String shortUrl;
    @Column
    private LocalDateTime startDate;
    @Column
    private LocalDateTime endDate;
    @Column
    private int views;
    @Column
    private Long userId;

    public UrlProfile(String fullUrl, Long userId) {
        this.fullUrl = fullUrl;
        this.shortUrl = generateShortUrl();
        this.views = 0;
        this.startDate = LocalDateTime.now();
        this.endDate = this.startDate.plusMonths(1);
        this.userId = userId;
    }

    public UrlProfile() {

    }

    private String generateShortUrl() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_";
        Random random = new Random();
        StringBuilder shortUrl = new StringBuilder();

        for (int i = 0; i < 8; i++) {
            int index = random.nextInt(characters.length());
            shortUrl.append(characters.charAt(index));
        }

        return shortUrl.toString();
    }
}

