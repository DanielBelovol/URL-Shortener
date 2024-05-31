package com.example.url_profile;

import lombok.Data;

@Data
public class UrlProfileRequest {
    private String fullUrl;
    private Long userId;
}
