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
import com.example.url_profile.UrlProfileUtil;
import com.example.url_profile.UrlProfileValidationService;
import com.example.url_view.UrlViewMapper;
import com.example.url_view.UrlViewService;
import com.example.user.User;
import com.example.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringVersion;
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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/V1/admin")
public class AdminModeratorController {
    private UrlProfileService urlProfileService;
    private UserService userService;
    private UrlProfileValidationService urlProfileValidationService;
    private UrlViewService urlViewService;
    private UrlViewMapper urlViewMapper;
    private UrlProfileUtil urlProfileUtil;

    @Autowired
    public AdminModeratorController(UrlProfileService urlProfileService, UserService userService, UrlProfileValidationService urlProfileValidationService, UrlViewService urlViewService, UrlViewMapper urlViewMapper, UrlProfileUtil urlProfileUtil) {
        this.urlProfileService = urlProfileService;
        this.userService = userService;
        this.urlProfileValidationService = urlProfileValidationService;
        this.urlViewService = urlViewService;
        this.urlViewMapper = urlViewMapper;
        this.urlProfileUtil = urlProfileUtil;
    }

    @PostMapping("")
    public ResponseEntity<?> createUrlProfile(@RequestBody UrlProfileRequest urlProfileRequest)
            throws UserNotFoundException {
        if (!urlProfileValidationService.isValidURL(urlProfileRequest.getFullUrl())) {
            return ResponseEntity.badRequest().body("Invalid URL");
        }


        if (userService.getUserById(urlProfileRequest.getUserId())==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        LocalDateTime now = LocalDateTime.now();

        UrlProfileDto dto = new UrlProfileDto(urlProfileRequest.getFullUrl(), null, now, now.plusMonths(1), urlProfileRequest.getUserId());

        User user = userService.getUserById(dto.getUserId());

        return ResponseEntity
                .status(201)
                .body(urlProfileService.createUrl(dto, user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> activateExpiredUrl(@PathVariable long id) {
        try {
            return ResponseEntity.ok(urlProfileService.activateExpiredUrl(id));
        } catch (UrlNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Url not found");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<UrlProfileResponse> getUrlProfile(@PathVariable long id)
            throws UrlNotFoundException {
        UrlProfileResponse urlProfileResponse = urlProfileService.getUrlById(id);

        if (urlProfileResponse == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity
                .ok()
                .body(urlProfileService.getUrlById(id));
    }

    @GetMapping("/urls/userId/{userId}")
    public ResponseEntity<List<UrlProfileView>> getAllUserUrlProfiles(@PathVariable Long userId) {
        List<UrlProfileResponse> urlProfileResponses = urlProfileService.getAllUserUrls(userId);
        List<UrlProfileView> urlProfileViews = new ArrayList<>();

        for (UrlProfileResponse urlProfileResponse : urlProfileResponses) {
            urlProfileViews.add(new UrlProfileView(
                    urlProfileResponse.getLongUrl(),
                    urlProfileResponse.getShortUrl(),
                    urlProfileResponse.getCreatedAt(),
                    urlProfileResponse.getValidTo(),
                    urlProfileResponse.getUser().getUsername(),
                    urlViewService.getCountOfRedirectsForUrl(urlProfileResponse.getId()),
                    urlViewService.getViewsForUrl(urlProfileResponse.getId()).stream().map(urlViewMapper::fromUrlViewResponseToDto).collect(Collectors.toList())
            ));
        }

        return ResponseEntity
                .ok()
                .body(urlProfileViews);
    }

    @GetMapping("/urls/active/{userId}")
    public ResponseEntity<List<UrlProfileView>> getAllUserActiveUrls(@PathVariable Long userID) {
        List<UrlProfileResponse> urlProfileResponses = urlProfileService.getAllUserActiveUrls(userID);
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
    @GetMapping("")
    public ResponseEntity<List<UrlProfileView>> getAllUrls() {
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
        String os = urlProfileUtil.extractOsFromUserAgent(userAgent);
        String browser = urlProfileUtil.extractBrowserFromUserAgent(userAgent);

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

        if (urlProfile == null || !urlProfile.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("URL not found or not owned by user");
        }

        urlProfileService.deleteByShortUrl(shortUrl);
        return ResponseEntity.ok().body("Deleted successfully");
    }
    @DeleteMapping("/deleteUser/userId/{userId}")
    public ResponseEntity<?> deleteUserById(@PathVariable Long userId)  {
        try {
            userService.getUserById(userId);
        }catch (UserNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        userService.deleteUserById(userId);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User was deleted");
    }
}
