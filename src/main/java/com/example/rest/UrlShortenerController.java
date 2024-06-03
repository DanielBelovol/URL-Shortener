package com.example.rest;

import com.example.exceptions.UserNotFoundException;
import com.example.url_profile.*;
import com.example.url_view.UrlView;
import com.example.url_view.UrlViewService;
import com.example.user.User;

import com.example.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;


// todo
// change return types to ResponseEntity
// + mapping

@RestController
@RequestMapping("api/v1/url_profiles")
public class UrlShortenerController {
    private UrlProfileService urlProfileService;
    private UserService userService;
    private UrlProfileValidationService urlProfileValidationService;
    private UrlViewService urlViewService;

    @Autowired
    public UrlShortenerController(UrlProfileService urlProfileService, UserService userService, UrlProfileValidationService urlProfileValidationService, UrlViewService urlViewService) {
        this.urlProfileService = urlProfileService;
        this.userService = userService;
        this.urlProfileValidationService = urlProfileValidationService;
        this.urlViewService = urlViewService;
    }

    @PostMapping("")
    public ResponseEntity<?> addUrlProfile(@RequestBody UrlProfileRequest urlProfileRequest)
            throws UserNotFoundException {
        if (!urlProfileValidationService.isValidURL(urlProfileRequest.getFullUrl())) {
            return ResponseEntity.badRequest().body("Invalid URL");
        }

        User user = userService.getUserById(urlProfileRequest.getUserId());

        UrlProfile urlProfile = new UrlProfile(urlProfileRequest.getFullUrl(), user);
        urlProfileService.save(urlProfile);
        return ResponseEntity
                .status(201)
                .body(urlProfile);
    }

    @PutMapping("")
    public ResponseEntity<?> activateExpiredUrl(@RequestBody long id){
        urlProfileService.activateExpiredUrl(id);

        return ResponseEntity
                .ok()
                .body("Url was activated");
    }

    @GetMapping("/{id}")
    public UrlProfileDTO getUrlProfile(@PathVariable Long id) {
        return urlProfileService.getById(id);
    }

    @GetMapping("")
    public List<UrlProfile> getAllUrlProfiles() {
        return urlProfileService.getAllUrls();
    }

    @GetMapping("/active")
    public ResponseEntity<List<UrlProfile>> getAllActiveUrls(){
        return ResponseEntity.ok().body(urlProfileService.getAllActiveUrls());
    }

    @GetMapping("/{shortUrl}")
    public RedirectView redirectToFullUrl(@PathVariable String shortUrl,
                                          HttpServletRequest request,
                                          @RequestHeader("User-Agent") String userAgent) {
        UrlProfile urlProfile = urlProfileService.getByShortUrl(shortUrl);

        String ipAddress = request.getRemoteAddr();
        String os = extractOsFromUserAgent(userAgent);
        String browser = extractBrowserFromUserAgent(userAgent);

        UrlView urlView = new UrlView(urlProfile, ipAddress, os, browser, "");
        urlViewService.saveUrlView(urlView);

        return new RedirectView(urlProfile.getLongUrl());
    }

    @DeleteMapping("/{shortUrl}")
    public void deleteByUrl(@PathVariable String shortUrl) {
        urlProfileService.deleteByShortUrl(shortUrl);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable long id){
        urlProfileService.deleteById(id);
        return ResponseEntity
                .ok()
                .body("Deleted successfully");
    }

    private String extractOsFromUserAgent(String userAgent) {
        if (userAgent.toLowerCase().contains("windows")) {
            return "Windows";
        } else if (userAgent.toLowerCase().contains("mac")) {
            return "Mac";
        } else if (userAgent.toLowerCase().contains("x11")) {
            return "Unix";
        } else if (userAgent.toLowerCase().contains("android")) {
            return "Android";
        } else if (userAgent.toLowerCase().contains("iphone")) {
            return "iPhone";
        } else {
            return "Unknown";
        }
    }

    private String extractBrowserFromUserAgent(String userAgent) {
        if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {
            return "Internet Explorer";
        } else if (userAgent.contains("Edge")) {
            return "Edge";
        } else if (userAgent.contains("Firefox")) {
            return "Firefox";
        } else if (userAgent.contains("Chrome") && !userAgent.contains("Chromium")) {
            return "Chrome";
        } else if (userAgent.contains("Safari") && !userAgent.contains("Chrome")) {
            return "Safari";
        } else if (userAgent.contains("Opera") || userAgent.contains("OPR")) {
            return "Opera";
        } else {
            return "Unknown";
        }
    }
}
