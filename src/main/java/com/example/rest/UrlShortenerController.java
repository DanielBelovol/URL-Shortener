package com.example.rest;

import com.example.data.url_profile.UrlProfileDto;
import com.example.data.url_profile.UrlProfileRequest;
import com.example.data.url_profile.UrlProfileResponse;
import com.example.data.url_profile_views.UrlProfileView;
import com.example.data.url_view.UrlViewDto;
import com.example.exceptions.UrlExpiredException;
import com.example.exceptions.UrlNotFoundException;
import com.example.exceptions.UserNotFoundException;
import com.example.url_profile.UrlProfile;
import com.example.url_profile.UrlProfileService;
import com.example.url_profile.UrlProfileValidationService;
import com.example.url_view.UrlViewMapper;
import com.example.url_view.UrlViewService;
import com.example.user.User;
import com.example.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("V1/urls")
public class UrlShortenerController {
    private UrlProfileService urlProfileService;
    private UserService userService;
    private UrlProfileValidationService urlProfileValidationService;
    private UrlViewService urlViewService;
    private UrlViewMapper urlViewMapper;

    @Autowired
    public UrlShortenerController(UrlProfileService urlProfileService, UserService userService, UrlProfileValidationService urlProfileValidationService, UrlViewService urlViewService, UrlViewMapper urlViewMapper) {
        this.urlProfileService = urlProfileService;
        this.userService = userService;
        this.urlProfileValidationService = urlProfileValidationService;
        this.urlViewService = urlViewService;
        this.urlViewMapper = urlViewMapper;
    }

    @PostMapping("")
    public ResponseEntity<?> createUrlProfile(@RequestBody UrlProfileRequest urlProfileRequest)
            throws UserNotFoundException {
        if (!urlProfileValidationService.isValidURL(urlProfileRequest.getFullUrl())) {
            return ResponseEntity.badRequest().body("Invalid URL");
        }
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//        String username = userDetails.getUsername();

        LocalDateTime now = LocalDateTime.now();

        UrlProfileDto dto = new UrlProfileDto(urlProfileRequest.getFullUrl(), null, now, now.plusMonths(1), urlProfileRequest.getUserId());

        User user = userService.getUserById(dto.getUserId());

        return ResponseEntity
                .status(201)
                .body(urlProfileService.createUrl(dto, user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> activateExpiredUrl(@PathVariable long id)
            throws UrlNotFoundException {
        return ResponseEntity
                .ok()
                .body(urlProfileService.activateExpiredUrl(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UrlProfileResponse> getUrlProfile(@PathVariable long id)
            throws UrlNotFoundException {
        return ResponseEntity
                .ok()
                .body(urlProfileService.getUrlById(id));
    }

    @GetMapping("")
    public ResponseEntity<List<UrlProfileView>> getAllUrlProfiles() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        User user = userService.getUserByUsername(username);
        Set<UrlProfile> urls = user.getUrls();

        List<UrlProfileView> urlProfileViews = new ArrayList<>();
        for (UrlProfile urlProfile : urls) {
            urlProfileViews.add(new UrlProfileView(
                    urlProfile.getLongUrl(),
                    urlProfile.getShortUrl(),
                    urlProfile.getCreatedAt(),
                    urlProfile.getValidTo(),
                    urlProfile.getUser().getUsername(),
                    urlViewService.getCountOfRedirectsForUrl(urlProfile.getId()),
                    urlViewService.getViewsForUrl(urlProfile.getId())
                            .stream()
                            .map(urlViewMapper::fromUrlViewResponseToDto)
                            .collect(Collectors.toList())
            ));
        }

        return ResponseEntity
                .ok()
                .body(urlProfileViews);
    }

    @GetMapping("/active")
    public ResponseEntity<List<UrlProfileView>> getAllActiveUrls() {
        List<UrlProfileResponse> urlProfileResponses = urlProfileService.getAllActiveUrls();
        List<UrlProfileView> urlProfileViews = new ArrayList<>();

        for(UrlProfileResponse urlProfileResponse : urlProfileResponses){
            urlProfileViews.add(new UrlProfileView(
                    urlProfileResponse.getLongUrl(),
                    urlProfileResponse.getShortUrl(),
                    urlProfileResponse.getCreatedAt(),
                    urlProfileResponse.getValidTo(),
                    urlProfileResponse.getUser().getUsername(),
                    urlViewService.getCountOfRedirectsForUrl(urlProfileResponse.getId()),
                    urlViewService.getViewsForUrl(urlProfileResponse.getId()).stream().map(urlViewMapper::fromUrlViewResponseToDto).collect(Collectors.toList())));
        }

        return ResponseEntity
                .ok()
                .body(urlProfileViews);
    }

    @GetMapping("/redir/{shortUrl}")
    public RedirectView redirectToFullUrl(@PathVariable String shortUrl,
                                          HttpServletRequest request,
                                          @RequestHeader("User-Agent") String userAgent)
            throws UrlExpiredException {
        UrlProfileResponse urlProfileResponse = urlProfileService.getByShortUrl(shortUrl);

        if(urlProfileResponse.getValidTo().isBefore(LocalDateTime.now())) throw new UrlExpiredException();

        String ipAddress = request.getRemoteAddr();
        String os = extractOsFromUserAgent(userAgent);
        String browser = extractBrowserFromUserAgent(userAgent);

        UrlViewDto dto = new UrlViewDto(ipAddress, os, browser, "");
        urlViewService.saveUrlView(dto, urlProfileResponse);

        return new RedirectView(urlProfileResponse.getLongUrl());
    }

    @DeleteMapping("/{shortUrl}")
    public ResponseEntity<?> deleteByUrl(@PathVariable String shortUrl) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        User user = userService.getUserByUsername(username);

        UrlProfile urlProfile = urlProfileService.getUrlProfileByShortUrl(shortUrl);
        boolean isUserUrl = user.getUrls().stream().anyMatch(profile -> profile.getShortUrl().equals(shortUrl));
        if (!isUserUrl) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Url not found");
        }
        urlProfileService.deleteByShortUrl(shortUrl);
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
