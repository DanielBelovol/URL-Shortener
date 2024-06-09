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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/v1/urls")
public class UrlShortenerController {
    private final UrlProfileService urlProfileService;
    private final UserService userService;
    private final UrlProfileValidationService urlProfileValidationService;
    private final UrlViewService urlViewService;
    private final UrlViewMapper urlViewMapper;

    @Autowired
    public UrlShortenerController(UrlProfileService urlProfileService, UserService userService, UrlProfileValidationService urlProfileValidationService, UrlViewService urlViewService, UrlViewMapper urlViewMapper) {
        this.urlProfileService = urlProfileService;
        this.userService = userService;
        this.urlProfileValidationService = urlProfileValidationService;
        this.urlViewService = urlViewService;
        this.urlViewMapper = urlViewMapper;
    }

    @PostMapping("")
    public ResponseEntity<?> createUrlProfile(@RequestBody UrlProfileRequest urlProfileRequest, Principal principal)
            throws UserNotFoundException {
        if (!urlProfileValidationService.isValidURL(urlProfileRequest.getFullUrl())) {
            return ResponseEntity.badRequest().body("Invalid URL");
        }

        User user = userService.getUserByEmail(principal.getName());
        LocalDateTime now = LocalDateTime.now();

        UrlProfileDto dto = new UrlProfileDto(urlProfileRequest.getFullUrl(), null, now, now.plusMonths(1), user.getId());

        return ResponseEntity
                .status(201)
                .body(urlProfileService.createUrl(dto, user));
    }

    @PutMapping("/{id}")
    @PreAuthorize("@urlProfileService.isOwner(#id, principal.username)")
    public ResponseEntity<?> activateExpiredUrl(@PathVariable long id, Principal principal)
            throws UrlNotFoundException {
        return ResponseEntity
                .ok()
                .body(urlProfileService.activateExpiredUrl(id));
    }

    @GetMapping("/{id}")
    @PreAuthorize("@urlProfileService.isOwner(#id, principal.username)")
    public ResponseEntity<UrlProfileResponse> getUrlProfile(@PathVariable long id, Principal principal)
            throws UrlNotFoundException {
        return ResponseEntity
                .ok()
                .body(urlProfileService.getUrlById(id));
    }

    @GetMapping("")
    public ResponseEntity<List<UrlProfileView>> getAllUrlProfiles(Principal principal) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//        String username = userDetails.getUsername();
        User user = userService.getUserByEmail(principal.getName());
        List<UrlProfileResponse> urls = urlProfileService.getAllUrlsByUserId(user.getId());

        List<UrlProfileView> urlProfileViews = new ArrayList<>();
        for (UrlProfileResponse urlProfile : urls) {
            urlProfileViews.add(new UrlProfileView(
                    urlProfile.getLongUrl(),
                    urlProfile.getShortUrl(),
                    urlProfile.getCreatedAt(),
                    urlProfile.getValidTo(),
                    urlProfile.getCreatedBy(),
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
    public ResponseEntity<List<UrlProfileView>> getAllActiveUrls(Principal principal) {
        User user = userService.getUserByEmail(principal.getName());

        List<UrlProfileResponse> urlProfileResponses = urlProfileService.getAllActiveUserUrls(user.getId());
        List<UrlProfileView> urlProfileViews = new ArrayList<>();

        for(UrlProfileResponse urlProfileResponse : urlProfileResponses){
            urlProfileViews.add(new UrlProfileView(
                    urlProfileResponse.getLongUrl(),
                    urlProfileResponse.getShortUrl(),
                    urlProfileResponse.getCreatedAt(),
                    urlProfileResponse.getValidTo(),
                    urlProfileResponse.getCreatedBy(),
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
            throws UrlExpiredException, UserNotFoundException {
        UrlProfileResponse urlProfileResponse = urlProfileService.getByShortUrl(shortUrl);

        if(urlProfileResponse.getValidTo().isBefore(LocalDateTime.now())) throw new UrlExpiredException();

        String ipAddress = request.getRemoteAddr();
        String os = extractOsFromUserAgent(userAgent);
        String browser = extractBrowserFromUserAgent(userAgent);
        String referer = request.getHeader("referer") != null ? request.getHeader("referer") : "";
        UrlViewDto dto = new UrlViewDto(ipAddress, os, browser, referer);
        urlViewService.saveUrlView(dto, urlProfileResponse);

        return new RedirectView(urlProfileResponse.getLongUrl());
    }

    @DeleteMapping("/{shortUrl}")
    @PreAuthorize("@urlProfileService.isOwnerByUrl(#shortUrl, principal.username)")
    public ResponseEntity<?> deleteByUrl(@PathVariable String shortUrl, Principal principal) {
        UrlProfile urlProfile = urlProfileService.getUrlProfileByShortUrl(shortUrl);
        if (urlProfile == null) {
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
