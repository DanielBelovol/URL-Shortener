package com.example.data.url_profile;

import com.example.url_view.UrlView;
import com.example.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UrlProfileResponse implements Serializable {
    private Long id;
    private String longUrl;
    private String shortUrl;
    private LocalDateTime createdAt;
    private LocalDateTime validTo;
    private Long createdBy;
    private Set<UrlView> urlViews = new HashSet<>();
}
