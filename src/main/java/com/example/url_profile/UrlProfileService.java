package com.example.url_profile;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UrlProfileService {

    @Autowired
    private UrlProfileRepository urlProfileRepository;

    @Autowired
    public UrlProfileService() {

    }

    public UrlProfile save(UrlProfile urlProfile) {
        urlProfileRepository.save(urlProfile);
        return urlProfile;
    }
    public List<UrlProfile> getAll() {
        return urlProfileRepository.findAll();
    }

    public UrlProfileDTO getById(Long id) {
        UrlProfile urlProfile = urlProfileRepository.getReferenceById(id);
        return convertUrlProfileEntityToDto(urlProfile);
    }

    public UrlProfile getByShortUrl(String shortUrl) {
        return urlProfileRepository.findByShortUrl(shortUrl);
    }

    @Transactional
    public void deleteByShortUrl(String shortUrl) {
        urlProfileRepository.deleteUrlProfileByShortUrl(shortUrl);
    }
    public UrlProfileDTO convertUrlProfileEntityToDto(UrlProfile urlProfile) {
        return new UrlProfileDTO(urlProfile.getId(), urlProfile.getFullUrl(), urlProfile.getShortUrl(), urlProfile.getStartDate(), urlProfile.getEndDate(), urlProfile.getViews(), 1L);
    }
}
