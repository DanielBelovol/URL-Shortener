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
@RequestMapping("api/v1/url_profiles")
public class UrlShortenerController {

    @Autowired
    private UrlProfileService urlProfileService;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/{id}")
    public UrlProfileDTO getUrlProfile(@PathVariable Long id) {
        return urlProfileService.getById(id);
    }

    @GetMapping("/")
    public List<UrlProfile> getAllUrlProfiles() {
        return urlProfileService.getAll();
    }

    @PostMapping("/")
    public UrlProfile addUrlProfile(@RequestBody UrlProfileRequest urlProfileRequest) {
        User user = userRepository.findById(urlProfileRequest.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));
        UrlProfile urlProfile = new UrlProfile(urlProfileRequest.getFullUrl(), user);
        urlProfileService.save(urlProfile);
        return urlProfile;
    }

    @GetMapping("/{shortUrl}")
    public RedirectView redirectToFullUrl(@PathVariable String shortUrl) {
        UrlProfile urlProfile = urlProfileService.getByShortUrl(shortUrl);
        urlProfile.setViews(urlProfile.getViews() + 1);
        urlProfileService.save(urlProfile);
        return new RedirectView(urlProfile.getLongUrl());
    }

    @DeleteMapping("/{shortUrl}")
    public void deleteByUrl(@PathVariable String shortUrl) {
        urlProfileService.deleteByShortUrl(shortUrl);
    }
}
