package com.example.url_profile;

import com.example.data.url_profile.UrlProfileDto;
import com.example.data.url_profile.UrlProfileResponse;
import com.example.exceptions.UrlNotFoundException;
import com.example.user.User;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class UrlProfileService {
    private final UrlProfileRepository urlProfileRepository;
    private final UrlProfileMapper urlProfileMapper;

    @Autowired
    public UrlProfileService(UrlProfileRepository urlProfileRepository, UrlProfileMapper urlProfileMapper) {
        this.urlProfileRepository = urlProfileRepository;
        this.urlProfileMapper = urlProfileMapper;
    }

    @Caching(evict = {
            @CacheEvict(value = "UrlProfileService::getAllActiveUrls", allEntries = true),
            @CacheEvict(value = "UrlProfileService::getAllUrlsByUserId", allEntries = true),
            @CacheEvict(value = "UrlProfileService::getAllUrls", allEntries = true)
    })
    public UrlProfileResponse createUrl(UrlProfileDto dto, User user) {
        UrlProfile urlProfile = urlProfileMapper.fromUrlProfileDtoToEntity(dto);
        urlProfile.setShortUrl(generateUniqueShortUrl());
        urlProfile.setUser(user);

        return urlProfileMapper.fromUrlProfileEntityToResponse(save(urlProfile));
    }

    @Cacheable(value = "UrlProfileService::getAllActiveUrls")
    public List<UrlProfileResponse> getAllActiveUrls() {
        return urlProfileRepository.getAllActiveUrls().stream()
                .map(urlProfileMapper::fromUrlProfileEntityToResponse)
                .collect(Collectors.toList());
    }
    public List<UrlProfileResponse> getAllActiveUserUrls(Long userId) {
        return getAllActiveByUserId(userId).stream()
                .map(urlProfileMapper::fromUrlProfileEntityToResponse)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "UrlProfileService::getAllUrls")
    public List<UrlProfileResponse> getAllUrls() {
        return urlProfileRepository.findAll().stream()
                .map(urlProfileMapper::fromUrlProfileEntityToResponse)
                .collect(Collectors.toList());
    }
    @Cacheable(value = "UrlProfileService::getAllUrlsByUserId")
    public List<UrlProfileResponse> getAllUrlsByUserId(Long userId) {
        return urlProfileRepository.findAllByUserId(userId).stream()
                .map(urlProfileMapper::fromUrlProfileEntityToResponse)
                .collect(Collectors.toList());
    }

    public UrlProfileResponse getUrlById(long id) throws UrlNotFoundException {
        UrlProfile urlProfile = getById(id);
        return urlProfileMapper.fromUrlProfileEntityToResponse(urlProfile);
    }

    public UrlProfileResponse activateExpiredUrl(long id) throws UrlNotFoundException {
        UrlProfile urlProfile = getById(id);
        urlProfile.setValidTo(LocalDateTime.now().plusMonths(1));

        return urlProfileMapper.fromUrlProfileEntityToResponse(save(urlProfile));
    }

    public UrlProfileResponse getByShortUrl(String shortUrl) {
        return urlProfileMapper.fromUrlProfileEntityToResponse(getUrlProfileByShortUrl(shortUrl));
    }


    @Caching(evict = {
            @CacheEvict(value = "UrlProfileService::getUrlProfileByShortUrl", key="#urlProfile.shortUrl"),
            @CacheEvict(value = "UrlProfileService::existsByShortUrl", key="#urlProfile.shortUrl"),
            @CacheEvict(value = "UrlProfileService::getById", key="#urlProfile.id"),
            @CacheEvict(value = "UrlProfileService::getAllActiveUrls", allEntries = true),
            @CacheEvict(value = "UrlProfileService::getAllUrlsByUserId", allEntries = true),
            @CacheEvict(value = "UrlProfileService::getAllUrls", allEntries = true)
    })
    public UrlProfile save(UrlProfile urlProfile){
        return urlProfileRepository.save(urlProfile);
    }

    @Cacheable(value = "UrlProfileService::getById", key="#id")
    public UrlProfile getById(Long id) throws UrlNotFoundException {
        return urlProfileRepository.findById(id).orElseThrow(() -> new UrlNotFoundException(id));
    }


    @Cacheable(value = "UrlProfileService::getUrlProfileByShortUrl", key="#shortUrl")
    public UrlProfile getUrlProfileByShortUrl(String shortUrl) {
        return urlProfileRepository.findByShortUrl(shortUrl);
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "UrlProfileService::getUrlProfileByShortUrl", key="#shortUrl"),
            @CacheEvict(value = "UrlProfileService::existsByShortUrl", key="#shortUrl"),
            @CacheEvict(value = "UrlProfileService::getAllActiveUrls", allEntries = true),
            @CacheEvict(value = "UrlProfileService::getAllUrlsByUserId", allEntries = true),
            @CacheEvict(value = "UrlProfileService::getAllUrls", allEntries = true)
    })
    public void deleteByShortUrl(String shortUrl) {
        urlProfileRepository.deleteUrlProfileByShortUrl(shortUrl);
    }

    @Cacheable(value = "UrlProfileService::existsByShortUrl", key="#shortUrl")
    public Boolean existsByShortUrl(String shortUrl){
        return urlProfileRepository.existsByShortUrl(shortUrl);
    }
    private String generateUniqueShortUrl() {
        String shortUrl;
        do {
            shortUrl = UrlProfileUtil.generateShortUrl(6 + new Random().nextInt(3));
        } while (existsByShortUrl(shortUrl));
        return shortUrl;
    }

    @Cacheable(value = "UrlProfileService::getAllActiveByUserId", key="#userId")
    public List<UrlProfile> getAllActiveByUserId(Long userId){
        return urlProfileRepository.getAllActiveUrlsByUserId(userId);
    }

    public boolean isOwner(Long id, String userEmail) throws UrlNotFoundException {
        UrlProfile urlProfile = getById(id);
        return urlProfile.getUser().getEmail().equals(userEmail);
    }
    public boolean isOwnerByUrl(String shortUrl, String userEmail) throws UrlNotFoundException {
        UrlProfile urlProfile = getUrlProfileByShortUrl(shortUrl);
        return urlProfile.getUser().getEmail().equals(userEmail);
    }
}
