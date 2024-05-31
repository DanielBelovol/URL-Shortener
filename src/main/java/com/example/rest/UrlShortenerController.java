package com.example.rest;

import com.example.url_profile.UrlProfile;
import com.example.url_profile.UrlProfileDTO;
import com.example.url_profile.UrlProfileRequest;
import com.example.url_profile.UrlProfileService;
import com.example.user.User;
import com.example.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@RestController
public class UrlShortenerController {

    @Autowired
    private UrlProfileService urlProfileService;
    @Autowired
    private UserRepository userRepository;

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
        User user = userRepository.findById(urlProfileRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        UrlProfile urlProfile = new UrlProfile(urlProfileRequest.getFullUrl(), user);
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
