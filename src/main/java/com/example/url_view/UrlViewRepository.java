package com.example.url_view;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UrlViewRepository extends JpaRepository<UrlView, Long> {
    @Query("SELECT uv FROM UrlView uv WHERE uv.urlProfile.id = :urlId")
    List<UrlView> findAllByUrlId(@Param("urlId") Long urlId);

    @Query("SELECT COUNT(uv) FROM UrlView uv WHERE uv.urlProfile.id = :urlId")
    Long countByUrlId(@Param("urlId") Long urlId);
}
