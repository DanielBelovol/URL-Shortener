package com.example.url_profile;

import com.example.testcontainer.BaseTestWithPostgresContainer;
import com.example.user.User;
import com.example.data.url_profile.UrlProfileDto;
import com.example.data.url_profile.UrlProfileResponse;
import com.example.data.url_profile.UrlProfileRequest;
import com.example.exceptions.UserNotFoundException;
import com.example.user.UserService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UrlProfileMapperTest extends BaseTestWithPostgresContainer {
    @Mock
    private UserService userService;
    @InjectMocks
    private UrlProfileMapper urlProfileMapper;

    @Test
    void fromUrlProfileEntityToDtoTest() {
        LocalDateTime now = LocalDateTime.now();
        User user = new User();
        user.setId(1L);
        UrlProfile urlProfile = new UrlProfile(1L, "https://www.youtube.com/", "1234abc", now, now.plusMonths(1), user, Collections.emptySet());

        UrlProfileDto expectedDto = new UrlProfileDto("https://www.youtube.com/", "1234abc", now, now.plusMonths(1), user.getId());

        UrlProfileDto actualDto = urlProfileMapper.fromUrlProfileEntityToDto(urlProfile);

        Assertions.assertEquals(expectedDto, actualDto);
    }

    @Test
    void fromUrlProfileDtoToEntityTest() throws UserNotFoundException {
        LocalDateTime now = LocalDateTime.now();
        User user = new User();
        user.setId(1L);
        user.setUsername("testUser");
        when(userService.getUserById(anyLong())).thenReturn(user);
        UrlProfileDto dto = new UrlProfileDto("https://www.youtube.com/", "1234abc", now, now.plusMonths(1), user.getId());

        UrlProfile expectedUrl = new UrlProfile(null, "https://www.youtube.com/", "1234abc", now, now.plusMonths(1), user, Collections.emptySet());

        UrlProfile actualUrl = urlProfileMapper.fromUrlProfileDtoToEntity(dto);

        Assertions.assertEquals(expectedUrl, actualUrl);
    }

    @Test
    void fromUrlProfileRequestToDtoTest() throws UserNotFoundException {
        User user = new User();
        user.setId(5L);
        user.setUsername("testUser");
        when(userService.getUserById(anyLong())).thenReturn(user);
        UrlProfileRequest urlProfileRequest = new UrlProfileRequest("https://www.youtube.com/", 5L);
        String username = userService.getUserById(urlProfileRequest.getUserId()).getUsername();
        UrlProfileDto expectedDto = new UrlProfileDto("https://www.youtube.com/", null, null, null, user.getId());

        UrlProfileDto actualDto = urlProfileMapper.fromUrlProfileRequestToDto(urlProfileRequest);

        Assertions.assertEquals(expectedDto, actualDto);
    }

    @Test
    void fromUrlProfileEntityToResponseTest() throws UserNotFoundException {
        LocalDateTime now = LocalDateTime.now();
        User user = new User();
        user.setId(1L);
        user.setUsername("testUser");

        UrlProfile urlProfile = new UrlProfile(1L, "https://www.youtube.com/", "1234abc", now, now.plusMonths(1), user, Collections.emptySet());

        UrlProfileResponse expectedResponse = new UrlProfileResponse(1L, "https://www.youtube.com/", "1234abc", now, now.plusMonths(1), user.getId(), Collections.emptySet());

        UrlProfileResponse actualResponse = urlProfileMapper.fromUrlProfileEntityToResponse(urlProfile);

        Assertions.assertEquals(expectedResponse, actualResponse);
    }

    @SneakyThrows
    @Test
    void fromUrlProfileResponseToEntityTest() {
        LocalDateTime now = LocalDateTime.now();
        User user = new User();
        user.setId(1L);
        user.setUsername("testUser");
        when(userService.getUserById(anyLong())).thenReturn(user);
        UrlProfileResponse urlProfileResponse = new UrlProfileResponse(1L, "https://www.youtube.com/", "1234abc", now, now.plusMonths(1), user.getId(), Collections.emptySet());

        UrlProfile expectedUrl = new UrlProfile(1L, "https://www.youtube.com/", "1234abc", now, now.plusMonths(1), user, Collections.emptySet());

        UrlProfile actualUrl = urlProfileMapper.fromUrlProfileResponseToEntity(urlProfileResponse);

        Assertions.assertEquals(expectedUrl, actualUrl);
    }
}
