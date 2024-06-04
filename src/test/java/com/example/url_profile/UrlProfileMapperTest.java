package com.example.url_profile;

import com.example.data.url_profile.UrlProfileDto;
import com.example.data.url_profile.UrlProfileRequest;
import com.example.data.url_profile.UrlProfileResponse;
import com.example.user.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class UrlProfileMapperTest {

    UrlProfileMapper urlProfileMapper = new UrlProfileMapper();

    @Test
    void fromUrlProfileEntityToDtoTest() {
        LocalDateTime now = LocalDateTime.now();
        User user = new User();
        user.setId(10L);

        UrlProfile urlProfile = new UrlProfile(1L, "https://www.youtube.com/", "1234abc", now, now.plusMonths(1), user, Collections.emptySet());

        UrlProfileDto expectedDto = new UrlProfileDto("https://www.youtube.com/", "1234abc", now, now.plusMonths(1), 10L);

        UrlProfileDto actualDto = urlProfileMapper.fromUrlProfileEntityToDto(urlProfile);

        Assertions.assertEquals(expectedDto, actualDto);
    }

    @Test
    void fromUrlProfileDtoToEntityTest() {
        LocalDateTime now = LocalDateTime.now();
        UrlProfileDto dto = new UrlProfileDto("https://www.youtube.com/", "1234abc", now, now.plusMonths(1), 10L);

        UrlProfile expectedUrl = new UrlProfile(null, "https://www.youtube.com/", null, now, now.plusMonths(1), null, Collections.emptySet());

        UrlProfile actualUrl = urlProfileMapper.fromUrlProfileDtoToEntity(dto);

        Assertions.assertEquals(expectedUrl, actualUrl);
    }

    @Test
    void fromUrlProfileRequestToDtoTest() {
        UrlProfileRequest urlProfileRequest = new UrlProfileRequest("https://www.youtube.com/", 5L);

        UrlProfileDto expectedDto = new UrlProfileDto("https://www.youtube.com/", null, null, null, 5L);

        UrlProfileDto actualDto = urlProfileMapper.fromUrlProfileRequestToDto(urlProfileRequest);

        Assertions.assertEquals(expectedDto, actualDto);
    }

    @Test
    void fromUrlProfileEntityToResponseTest() {
        LocalDateTime now = LocalDateTime.now();
        User user = new User();
        user.setId(10L);

        UrlProfile urlProfile = new UrlProfile(1L, "https://www.youtube.com/", "1234abc", now, now.plusMonths(1), user, Collections.emptySet());

        UrlProfileResponse expectedResponse = new UrlProfileResponse(1L, "https://www.youtube.com/", "1234abc", now, now.plusMonths(1), user, Collections.emptySet());

        UrlProfileResponse actualResponse = urlProfileMapper.fromUrlProfileEntityToResponse(urlProfile);

        Assertions.assertEquals(expectedResponse, actualResponse);

    }

    @Test
    void fromUrlProfileResponseToEntityTest() {
        LocalDateTime now = LocalDateTime.now();
        User user = new User();
        user.setId(10L);

        UrlProfileResponse urlProfileResponse = new UrlProfileResponse(1L, "https://www.youtube.com/", "1234abc", now, now.plusMonths(1), user, Collections.emptySet());

        UrlProfile expectedUrl = new UrlProfile(1L, "https://www.youtube.com/", "1234abc", now, now.plusMonths(1), user, Collections.emptySet());

        UrlProfile actualUrl = urlProfileMapper.fromUrlProfileResponseToEntity(urlProfileResponse);

        Assertions.assertEquals(expectedUrl, actualUrl);
    }
}