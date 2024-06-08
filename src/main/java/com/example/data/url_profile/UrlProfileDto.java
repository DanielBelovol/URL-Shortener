package com.example.data.url_profile;

import com.example.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UrlProfileDto {
    private String longUrl;
    private String shortUrl;
    private LocalDateTime createdAt;
    private LocalDateTime validTo;
    private long userId;
}
