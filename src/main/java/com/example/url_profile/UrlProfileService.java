package com.example.url_profile;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
public class UrlProfileService {

    @Autowired
    private UrlProfileRepository urlProfileRepository;

    @Autowired
    public UrlProfileService() {

    }

    public UrlProfile save(UrlProfile urlProfile) {
        urlProfile.setShortUrl(generateUniqueShortUrl());
        urlProfileRepository.save(urlProfile);
        return urlProfile;
    }
    public List<UrlProfile> getAllActiveUrls(){
        return urlProfileRepository.getAllActiveUrls();
    }

    public List<UrlProfile> getAllUrls() {
        return urlProfileRepository.findAll();
    }

    public UrlProfileDTO getById(Long id) {
        UrlProfile urlProfile = urlProfileRepository.findById(id).orElse(null);
        return urlProfile != null ? convertUrlProfileEntityToDto(urlProfile) : null;
    }

    public void activateExpiredUrl(long id){
        UrlProfile urlProfile = urlProfileRepository.findById(id).orElseThrow(() -> new RuntimeException());

        urlProfile.setValidTo(LocalDateTime.now().plusMonths(1));

        urlProfileRepository.save(urlProfile);
    }

    public UrlProfile getByShortUrl(String shortUrl) {
        return urlProfileRepository.findByShortUrl(shortUrl);
    }

    @Transactional
    public void deleteByShortUrl(String shortUrl) {
        urlProfileRepository.deleteUrlProfileByShortUrl(shortUrl);
    }

    public boolean deleteById(long id){
        urlProfileRepository.deleteById(id);
        return true;
    }

    public UrlProfileDTO convertUrlProfileEntityToDto(UrlProfile urlProfile) {
        return new UrlProfileDTO(urlProfile.getId(), urlProfile.getLongUrl(), urlProfile.getShortUrl(), urlProfile.getCreatedAt(), urlProfile.getValidTo(), urlProfile.getUser() != null ? urlProfile.getUser().getId() : null);
    }

    private String generateUniqueShortUrl() {
        String shortUrl;
        do {
            shortUrl = UrlProfileUtil.generateShortUrl(6 + new Random().nextInt(3));
        } while (urlProfileRepository.existsByShortUrl(shortUrl));
        return shortUrl;
    }
}
