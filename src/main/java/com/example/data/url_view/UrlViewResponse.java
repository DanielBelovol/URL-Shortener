package com.example.data.url_view;

import com.example.url_profile.UrlProfile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UrlViewResponse {
    private long id;
    private UrlProfile urlProfile;
    private String ipAddress;
    private String osSystem;
    private String browser;
    private String referer;
}
