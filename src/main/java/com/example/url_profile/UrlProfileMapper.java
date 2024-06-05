package com.example.url_profile;

import com.example.data.url_profile.UrlProfileDto;
import com.example.data.url_profile.UrlProfileRequest;
import com.example.data.url_profile.UrlProfileResponse;
import org.springframework.stereotype.Component;

@Component
public class UrlProfileMapper {
    public UrlProfileDto fromUrlProfileEntityToDto(UrlProfile urlProfile){
        UrlProfileDto dto = new UrlProfileDto();

        dto.setLongUrl(urlProfile.getLongUrl());
        dto.setShortUrl(urlProfile.getShortUrl());
        dto.setCreatedAt(urlProfile.getCreatedAt());
        dto.setValidTo(urlProfile.getValidTo());
        dto.setUserId(urlProfile.getUser().getId());

        return dto;
    }
    public UrlProfile fromUrlProfileDtoToEntity(UrlProfileDto dto){
        UrlProfile urlProfile = new UrlProfile();

        urlProfile.setLongUrl(dto.getLongUrl());
        urlProfile.setCreatedAt(dto.getCreatedAt());
        urlProfile.setValidTo(dto.getValidTo());

        return urlProfile;
    }
    public UrlProfileDto fromUrlProfileRequestToDto(UrlProfileRequest request){
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
                urlProfile.getUser(),
                urlProfile.getUrlViews());
    }
    public UrlProfile fromUrlProfileResponseToEntity(UrlProfileResponse response){
        return new UrlProfile(
                response.getId(),
                response.getLongUrl(),
                response.getShortUrl(),
                response.getCreatedAt(),
                response.getValidTo(),
                response.getUser(),
                response.getUrlViews());
    }
}
