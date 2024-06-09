package com.example.url_profile;

import com.example.user.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UrlProfileRepository extends JpaRepository<UrlProfile, Long> {
    UrlProfile findByShortUrl(String shortUrl);
    @Transactional
    void deleteUrlProfileByShortUrl(String shortUrl);

    List<UrlProfile> findAllByUserId(Long userId);

    boolean existsByShortUrl(String shortUrl);

    @Query("SELECT u FROM UrlProfile u WHERE u.validTo > CURRENT_TIMESTAMP")
    List<UrlProfile> getAllActiveUrls();

    @Query("SELECT u FROM UrlProfile u WHERE u.validTo > CURRENT_TIMESTAMP AND u.user.id = :userId")
    List<UrlProfile> getAllActiveUrlsByUserId(@Param("userId") Long userId);
}
