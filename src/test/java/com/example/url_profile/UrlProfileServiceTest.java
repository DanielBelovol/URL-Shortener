package com.example.url_profile;

import com.example.data.url_profile.UrlProfileDto;
import com.example.data.url_profile.UrlProfileResponse;
import com.example.exceptions.UrlNotFoundException;
import com.example.exceptions.UserNotFoundException;
import com.example.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UrlProfileServiceTest {

    @Mock
    private UrlProfileRepository urlProfileRepository;

    @Mock
    private UrlProfileMapper urlProfileMapper;

    @InjectMocks
    private UrlProfileService urlProfileService;

    private UrlProfileDto urlProfileDto;
    private UrlProfile urlProfile;
    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");

        urlProfileDto = new UrlProfileDto();
        urlProfileDto.setLongUrl("http://example.com");

        urlProfile = new UrlProfile();
        urlProfile.setId(1L);
        urlProfile.setShortUrl("abc123");
        urlProfile.setLongUrl("http://example.com");
        urlProfile.setUser(user);
        urlProfile.setValidTo(LocalDateTime.now().plusMonths(1));
    }

    @Test
    public void testCreateUrl() throws UserNotFoundException {
        when(urlProfileMapper.fromUrlProfileDtoToEntity(urlProfileDto)).thenReturn(urlProfile);
        when(urlProfileRepository.save(urlProfile)).thenReturn(urlProfile);
        when(urlProfileMapper.fromUrlProfileEntityToResponse(urlProfile)).thenReturn(new UrlProfileResponse());

        UrlProfileResponse response = urlProfileService.createUrl(urlProfileDto, user);

        assertNotNull(response);
        verify(urlProfileRepository, times(1)).save(urlProfile);
    }

    @Test
    public void testGetAllActiveUrls() {
        when(urlProfileRepository.getAllActiveUrls()).thenReturn(Arrays.asList(urlProfile));
        when(urlProfileMapper.fromUrlProfileEntityToResponse(urlProfile)).thenReturn(new UrlProfileResponse());

        List<UrlProfileResponse> responses = urlProfileService.getAllActiveUrls();

        assertEquals(1, responses.size());
        verify(urlProfileRepository, times(1)).getAllActiveUrls();
    }

    @Test
    public void testGetAllUrls() {
        when(urlProfileRepository.findAll()).thenReturn(Arrays.asList(urlProfile));
        when(urlProfileMapper.fromUrlProfileEntityToResponse(urlProfile)).thenReturn(new UrlProfileResponse());

        List<UrlProfileResponse> responses = urlProfileService.getAllUrls();

        assertEquals(1, responses.size());
        verify(urlProfileRepository, times(1)).findAll();
    }

    @Test
    public void testGetUrlById() throws UrlNotFoundException {
        when(urlProfileRepository.findById(1L)).thenReturn(Optional.of(urlProfile));
        when(urlProfileMapper.fromUrlProfileEntityToResponse(urlProfile)).thenReturn(new UrlProfileResponse());

        UrlProfileResponse response = urlProfileService.getUrlById(1L);

        assertNotNull(response);
        verify(urlProfileRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetUrlById_NotFound() {
        when(urlProfileRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UrlNotFoundException.class, () -> {
            urlProfileService.getUrlById(1L);
        });
        verify(urlProfileRepository, times(1)).findById(1L);
    }

    @Test
    public void testActivateExpiredUrl() throws UrlNotFoundException {
        when(urlProfileRepository.findById(1L)).thenReturn(Optional.of(urlProfile));
        when(urlProfileRepository.save(urlProfile)).thenReturn(urlProfile);
        when(urlProfileMapper.fromUrlProfileEntityToResponse(urlProfile)).thenReturn(new UrlProfileResponse());

        UrlProfileResponse response = urlProfileService.activateExpiredUrl(1L);

        assertNotNull(response);
        verify(urlProfileRepository, times(1)).findById(1L);
        verify(urlProfileRepository, times(1)).save(urlProfile);
    }

    @Test
    public void testGetByShortUrl() {
        when(urlProfileRepository.findByShortUrl("abc123")).thenReturn(urlProfile);
        when(urlProfileMapper.fromUrlProfileEntityToResponse(urlProfile)).thenReturn(new UrlProfileResponse());

        UrlProfileResponse response = urlProfileService.getByShortUrl("abc123");

        assertNotNull(response);
        verify(urlProfileRepository, times(1)).findByShortUrl("abc123");
    }

    @Test
    public void testDeleteByShortUrl() {
        doNothing().when(urlProfileRepository).deleteUrlProfileByShortUrl("abc123");

        urlProfileService.deleteByShortUrl("abc123");

        verify(urlProfileRepository, times(1)).deleteUrlProfileByShortUrl("abc123");
    }

    @Test
    public void testGenerateUniqueShortUrl() throws Exception {
        Method method = UrlProfileService.class.getDeclaredMethod("generateUniqueShortUrl");
        method.setAccessible(true);
        String shortUrl = (String) method.invoke(urlProfileService);

        assertNotNull(shortUrl);
        assertTrue(shortUrl.length() >= 6 && shortUrl.length() <= 8);
    }
}