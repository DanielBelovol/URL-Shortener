package com.example.url_profile;

import com.example.user.User;
import com.example.url_profile.UrlProfile;
import com.example.url_profile.UrlProfileMapper;
import com.example.data.url_profile.UrlProfileDto;
import com.example.data.url_profile.UrlProfileResponse;
import com.example.data.url_profile.UrlProfileRequest;
import com.example.exceptions.UserNotFoundException;
import com.example.user.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.*;

class UrlProfileMapperTest {
    @Autowired
    private UserService userService;
    UrlProfileMapper urlProfileMapper = new UrlProfileMapper();

    @Test
    void fromUrlProfileEntityToDtoTest() {
        LocalDateTime now = LocalDateTime.now();
        String username = "testUser"; // Username
        UrlProfile urlProfile = new UrlProfile(1L, "https://www.youtube.com/", "1234abc", now, now.plusMonths(1), username, Collections.emptySet());

        UrlProfileDto expectedDto = new UrlProfileDto("https://www.youtube.com/", "1234abc", now, now.plusMonths(1), username);

        UrlProfileDto actualDto = urlProfileMapper.fromUrlProfileEntityToDto(urlProfile);

        Assertions.assertEquals(expectedDto, actualDto);
    }

    @Test
    void fromUrlProfileDtoToEntityTest() {
        LocalDateTime now = LocalDateTime.now();
        String username = "testUser"; // Username
        UrlProfileDto dto = new UrlProfileDto("https://www.youtube.com/", "1234abc", now, now.plusMonths(1), username);

        UrlProfile expectedUrl = new UrlProfile(null, "https://www.youtube.com/", "1234abc", now, now.plusMonths(1), username, Collections.emptySet());

        UrlProfile actualUrl = urlProfileMapper.fromUrlProfileDtoToEntity(dto);

        Assertions.assertEquals(expectedUrl, actualUrl);
    }

    @Test
    void fromUrlProfileRequestToDtoTest() throws UserNotFoundException {
        UrlProfileRequest urlProfileRequest = new UrlProfileRequest("https://www.youtube.com/", 5L);
        String username = userService.getUserById(urlProfileRequest.getUserId()).getUsername();
        UrlProfileDto expectedDto = new UrlProfileDto("https://www.youtube.com/", null, null, null, username);

        UrlProfileDto actualDto = urlProfileMapper.fromUrlProfileRequestToDto(urlProfileRequest);

        Assertions.assertEquals(expectedDto, actualDto);
    }

    @Test
    void fromUrlProfileEntityToResponseTest() {
        LocalDateTime now = LocalDateTime.now();
        String username = "testUser"; // Username
        UrlProfile urlProfile = new UrlProfile(1L, "https://www.youtube.com/", "1234abc", now, now.plusMonths(1), username, Collections.emptySet());

        UrlProfileResponse expectedResponse = new UrlProfileResponse(1L, "https://www.youtube.com/", "1234abc", now, now.plusMonths(1), username, Collections.emptySet());

        UrlProfileResponse actualResponse = urlProfileMapper.fromUrlProfileEntityToResponse(urlProfile);

        Assertions.assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void fromUrlProfileResponseToEntityTest() {
        LocalDateTime now = LocalDateTime.now();
        String username = "testUser"; // Username
        UrlProfileResponse urlProfileResponse = new UrlProfileResponse(1L, "https://www.youtube.com/", "1234abc", now, now.plusMonths(1), username, Collections.emptySet());

        UrlProfile expectedUrl = new UrlProfile(1L, "https://www.youtube.com/", "1234abc", now, now.plusMonths(1), username, Collections.emptySet());

        UrlProfile actualUrl = urlProfileMapper.fromUrlProfileResponseToEntity(urlProfileResponse);

        Assertions.assertEquals(expectedUrl, actualUrl);
    }
}
