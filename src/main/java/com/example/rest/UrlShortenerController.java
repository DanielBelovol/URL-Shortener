package com.example.rest;

import com.example.url_profile.UrlProfile;
import com.example.url_profile.UrlProfileDTO;
import com.example.url_profile.UrlProfileRequest;
import com.example.url_profile.UrlProfileService;
import com.example.user.User;
import com.example.user.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;


@RestController
@RequestMapping("api/v1/url")
public class UrlShortenerController {

    @Autowired
    private UrlProfileService urlProfileService;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/profile/{id}")
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

    @GetMapping("/redirect/{shortUrl}")
    public RedirectView redirectToFullUrl(@PathVariable String shortUrl) {
        UrlProfile urlProfile = urlProfileService.getByShortUrl(shortUrl);

        //End time
        LocalDateTime now = LocalDateTime.now();
        if (urlProfile.isLocked()){
            throw new RuntimeException("Url is locked because the time limit has been reached");
        }
        if (now.isAfter(urlProfile.getValidTo())) {
            urlProfile.setLocked(true);
            urlProfileService.save(urlProfile);
            throw new RuntimeException("The link has expired.");
        }

        urlProfile.setViews(urlProfile.getViews() + 1);
        urlProfileService.save(urlProfile);
        return new RedirectView(urlProfile.getLongUrl());
    }

    @DeleteMapping("/delete/{shortUrl}")
    public void deleteByUrl(@PathVariable String shortUrl) {
        urlProfileService.deleteByShortUrl(shortUrl);
    }
}
