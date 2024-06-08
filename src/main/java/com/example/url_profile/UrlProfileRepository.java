package com.example.url_profile;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UrlProfileRepository extends JpaRepository<UrlProfile, Long> {
    UrlProfile findByShortUrl(String shortUrl);
    @Transactional
    void deleteUrlProfileByShortUrl(String shortUrl);

    boolean existsByShortUrl(String shortUrl);

    @Query("SELECT u FROM UrlProfile u WHERE u.validTo > CURRENT_TIMESTAMP")
    List<UrlProfile> getAllActiveUrls();
}
