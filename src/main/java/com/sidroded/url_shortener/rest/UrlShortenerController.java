package com.sidroded.url_shortener.rest;

import com.sidroded.url_shortener.url_profile.UrlProfile;
import com.sidroded.url_shortener.url_profile.UrlProfileDTO;
import com.sidroded.url_shortener.url_profile.UrlProfileRequest;
import com.sidroded.url_shortener.url_profile.UrlProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@RestController
public class UrlShortenerController {

    @Autowired
    private UrlProfileService urlProfileService;

    @GetMapping("/v1/url_profile/{id}")
    public UrlProfileDTO getUrlProfile(@PathVariable Long id) {
        return urlProfileService.getById(id);
    }

    @GetMapping("/v1/")
    public List<UrlProfile> getAllUrlProfiles() {
        return urlProfileService.getAll();
    }

    @PostMapping("/v1/")
    public UrlProfile addUrlProfile(@RequestBody UrlProfileRequest urlProfileRequest) {
        UrlProfile urlProfile = new UrlProfile(urlProfileRequest.getFullUrl(), urlProfileRequest.getUserId());
        urlProfileService.save(urlProfile);
        return urlProfile;
    }

    @GetMapping("/v1/{shortUrl}")
    public RedirectView redirectToFullUrl(@PathVariable String shortUrl) {
        UrlProfile urlProfile = urlProfileService.getByShortUrl(shortUrl);
        urlProfile.setViews(urlProfile.getViews() + 1);
        urlProfileService.save(urlProfile);
        return new RedirectView(urlProfile.getFullUrl());
    }

    @PostMapping("/v1/{shortUrl}")
    public void deleteById(@PathVariable String shortUrl) {
        urlProfileService.deleteByShortUrl(shortUrl);
    }

}
