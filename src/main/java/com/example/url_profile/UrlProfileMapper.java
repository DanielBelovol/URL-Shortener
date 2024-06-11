package com.example.url_profile;

import com.example.data.url_profile.UrlProfileDto;
import com.example.data.url_profile.UrlProfileRequest;
import com.example.data.url_profile.UrlProfileResponse;
import com.example.exceptions.UserNotFoundException;
import com.example.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UrlProfileMapper {
    @Autowired
    private UserService userService;
    public UrlProfileDto fromUrlProfileEntityToDto(UrlProfile urlProfile){
        UrlProfileDto dto = new UrlProfileDto();

        dto.setLongUrl(urlProfile.getLongUrl());
        dto.setShortUrl(urlProfile.getShortUrl());
        dto.setCreatedAt(urlProfile.getCreatedAt());
        dto.setValidTo(urlProfile.getValidTo());
        dto.setUserId(urlProfile.getUser().getId());

        return dto;
    }
    public UrlProfile fromUrlProfileDtoToEntity(UrlProfileDto dto) throws UserNotFoundException {
        UrlProfile urlProfile = new UrlProfile();

        urlProfile.setShortUrl(dto.getShortUrl());
        urlProfile.setLongUrl(dto.getLongUrl());
        urlProfile.setCreatedAt(dto.getCreatedAt());
        urlProfile.setValidTo(dto.getValidTo());
        urlProfile.setUser(userService.getUserById(dto.getUserId()));

        return urlProfile;
    }
    public UrlProfileDto fromUrlProfileRequestToDto(UrlProfileRequest request) throws UserNotFoundException {
        UrlProfileDto dto = new UrlProfileDto();

        dto.setLongUrl(request.getFullUrl());
        dto.setUserId(request.getUserId());

        return dto;
    }
    public UrlProfileResponse fromUrlProfileEntityToResponse(UrlProfile urlProfile){
        return new UrlProfileResponse(
                urlProfile.getId(),
                urlProfile.getLongUrl(),
                urlProfile.getShortUrl(),
                urlProfile.getCreatedAt(),
                urlProfile.getValidTo(),
                urlProfile.getUser().getId(),
                urlProfile.getUrlViews());
    }
    public UrlProfile fromUrlProfileResponseToEntity(UrlProfileResponse response) throws UserNotFoundException {
        return new UrlProfile(
                response.getId(),
                response.getLongUrl(),
                response.getShortUrl(),
                response.getCreatedAt(),
                response.getValidTo(),
                userService.getUserById(response.getCreatedBy()),
                response.getUrlViews());
    }
}
