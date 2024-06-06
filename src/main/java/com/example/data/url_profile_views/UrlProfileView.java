package com.example.data.url_profile_views;

import com.example.data.url_view.UrlViewDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UrlProfileView {
    private String longUrl;
    private String shortUrl;
    private LocalDateTime createdAt;
    private LocalDateTime validTo;
    private String username;
    private long countOfRedirects;
    private List<UrlViewDto> views = new ArrayList<>();
}
