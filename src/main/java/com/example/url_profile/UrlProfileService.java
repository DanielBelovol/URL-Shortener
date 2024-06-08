package com.example.url_profile;

import com.example.data.url_profile.UrlProfileDto;
import com.example.data.url_profile.UrlProfileResponse;
import com.example.exceptions.UrlNotFoundException;
import com.example.user.User;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

// todo
// create user dto to return not full user

@Service
public class UrlProfileService {
    private UrlProfileRepository urlProfileRepository;
    private UrlProfileMapper urlProfileMapper;

    @Autowired
    public UrlProfileService(UrlProfileRepository urlProfileRepository, UrlProfileMapper urlProfileMapper) {
        this.urlProfileRepository = urlProfileRepository;
        this.urlProfileMapper = urlProfileMapper;
    }

    public UrlProfileResponse createUrl(UrlProfileDto dto, User user) {
        UrlProfile urlProfile = urlProfileMapper.fromUrlProfileDtoToEntity(dto);


        urlProfile.setShortUrl(generateUniqueShortUrl());
        urlProfile.setUser(user);

        user.getUrls().add(urlProfile);

        return urlProfileMapper.fromUrlProfileEntityToResponse(urlProfileRepository.save(urlProfile));
    }

    public List<UrlProfileResponse> getAllActiveUrls() {
        return urlProfileRepository.getAllActiveUrls().stream()
                .map(urlProfileMapper::fromUrlProfileEntityToResponse)
                .collect(Collectors.toList());
    }

    public List<UrlProfileResponse> getAllUrls() {
        return urlProfileRepository.findAll().stream()
                .map(urlProfileMapper::fromUrlProfileEntityToResponse)
                .collect(Collectors.toList());
    }

    public UrlProfileResponse getUrlById(long id) throws UrlNotFoundException {
        UrlProfile urlProfile = urlProfileRepository.findById(id)
                .orElseThrow(() -> new UrlNotFoundException(id));
        return urlProfileMapper.fromUrlProfileEntityToResponse(urlProfile);
    }

    public UrlProfileResponse activateExpiredUrl(long id) throws UrlNotFoundException {
        UrlProfile urlProfile = urlProfileRepository.findById(id).orElseThrow(() -> new UrlNotFoundException(id));
        urlProfile.setValidTo(LocalDateTime.now().plusMonths(1));

        return urlProfileMapper.fromUrlProfileEntityToResponse(urlProfileRepository.save(urlProfile));
    }

    public UrlProfileResponse getByShortUrl(String shortUrl) {
        return urlProfileMapper.fromUrlProfileEntityToResponse(urlProfileRepository.findByShortUrl(shortUrl));
    }

    public UrlProfile getUrlProfileByShortUrl(String shortUrl) {
        return urlProfileRepository.findByShortUrl(shortUrl);
    }

    @Transactional
    public void deleteByShortUrl(String shortUrl) {
        urlProfileRepository.deleteUrlProfileByShortUrl(shortUrl);
    }

    private String generateUniqueShortUrl() {
        String shortUrl;
        do {
            shortUrl = UrlProfileUtil.generateShortUrl(6 + new Random().nextInt(3));
        } while (urlProfileRepository.existsByShortUrl(shortUrl));
        return shortUrl;
    }
}
