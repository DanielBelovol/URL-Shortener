package com.sidroded.url_shortener.url_profile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UrlProfileDTO {
    private Long id;
    private String fullUrl;
    private String shortUrl;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private int views;
    private Long userId;
}
