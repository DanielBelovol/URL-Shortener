package com.example.data.url_profile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UrlProfileRequest {
    private String fullUrl;
    private Long userId;
}
