package com.example.url_profile;

import com.example.user.User;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Random;

@Data
@Entity
@Table(name = "url")
public class UrlProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "long_url")
    private String longUrl;

    @Column(name = "short_url")
    private String shortUrl;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "valid_to")
    private LocalDateTime validTo;

    @Column
    private int views;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User user;

    public UrlProfile(String longUrl, User user) {
        this.longUrl = longUrl;
        this.shortUrl = generateShortUrl();
        this.views = 0;
        this.createdAt = LocalDateTime.now();
        this.validTo = this.createdAt.plusMonths(1);
        this.user = user;
    }

    public UrlProfile() {}

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
