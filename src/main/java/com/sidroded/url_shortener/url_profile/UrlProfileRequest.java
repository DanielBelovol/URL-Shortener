package com.sidroded.url_shortener.url_profile;

import lombok.Data;

@Data
public class UrlProfileRequest {
    private String fullUrl;
    private Long userId;
}
