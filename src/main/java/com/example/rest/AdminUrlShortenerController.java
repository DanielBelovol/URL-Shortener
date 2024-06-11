package com.example.rest;

import com.example.data.url_profile.UrlProfileResponse;
import com.example.data.url_profile_views.UrlProfileView;
import com.example.exceptions.UrlNotFoundException;
import com.example.url_profile.UrlProfile;
import com.example.url_profile.UrlProfileService;
import com.example.url_profile.UrlProfileValidationService;
import com.example.url_view.UrlViewMapper;
import com.example.url_view.UrlViewService;
import com.example.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/v1/admin/urls")
public class AdminUrlShortenerController {
    private final UrlProfileService urlProfileService;
    private final UrlViewService urlViewService;
    private final UrlViewMapper urlViewMapper;

    @Autowired
    public AdminUrlShortenerController(UrlProfileService urlProfileService, UserService userService, UrlProfileValidationService urlProfileValidationService, UrlViewService urlViewService, UrlViewMapper urlViewMapper) {
        this.urlProfileService = urlProfileService;
        this.urlViewService = urlViewService;
        this.urlViewMapper = urlViewMapper;
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_MODERATOR') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> activateExpiredUrl(@PathVariable long id, Principal principal)
            throws UrlNotFoundException {
        log.info("User {} activated URL.", principal.getName());
        return ResponseEntity
                .ok()
                .body(urlProfileService.activateExpiredUrl(id));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<UrlProfileResponse> getUrlProfile(@PathVariable long id)
            throws UrlNotFoundException {
        return ResponseEntity
                .ok()
                .body(urlProfileService.getUrlById(id));
    }

    @GetMapping("")
    @PreAuthorize("hasRole('ROLE_MODERATOR') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<UrlProfileView>> getAllUrlProfiles() {
        List<UrlProfileResponse> urls = urlProfileService.getAllUrls();
        List<UrlProfileView> urlProfileViews = new ArrayList<>();
        List<Long> urlIds = urls.stream().map(UrlProfileResponse::getId).toList();
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
    @PreAuthorize("hasRole('ROLE_MODERATOR') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<UrlProfileView>> getAllActiveUrls() {
        List<UrlProfileResponse> urlProfileResponses = urlProfileService.getAllActiveUrls();
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

    @DeleteMapping("/{shortUrl}")
    @PreAuthorize("hasRole('ROLE_MODERATOR') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteByUrl(@PathVariable String shortUrl, Principal principal) {
        UrlProfile urlProfile = urlProfileService.getUrlProfileByShortUrl(shortUrl);
        if (urlProfile == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Url not found");
        }
        urlProfileService.deleteByShortUrl(shortUrl);
        log.info("User {} deleted URL.", principal.getName());
        return ResponseEntity
                .ok()
                .body("Deleted successfully");
    }
}
