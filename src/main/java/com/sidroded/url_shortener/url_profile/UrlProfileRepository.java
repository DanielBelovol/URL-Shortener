package com.sidroded.url_shortener.url_profile;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UrlProfileRepository extends JpaRepository<UrlProfile, Long> {
    UrlProfile findByShortUrl(String shortUrl);
    @Transactional
    void deleteUrlProfileByShortUrl(String shortUrl);
}
