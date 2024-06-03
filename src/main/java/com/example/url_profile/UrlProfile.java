package com.example.url_profile;

import com.example.url_view.UrlView;
import com.example.user.User;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

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

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User user;

    @OneToMany(mappedBy = "urlProfile")
    private Set<UrlView> urlViews = new HashSet<>();

    public UrlProfile() {}

    public UrlProfile(String longUrl, User user) {
        this.longUrl = longUrl;
        this.createdAt = LocalDateTime.now();
        this.validTo = this.createdAt.plusMonths(1);
        this.user = user;
    }

}
