package com.example.data.url_view;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UrlViewDto {
    private String ipAddress;
    private String osSystem;
    private String browser;
    private String referer;
}
