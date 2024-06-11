package com.example.url_view;

import com.example.data.url_profile.UrlProfileResponse;
import com.example.data.url_view.UrlViewDto;
import com.example.data.url_view.UrlViewResponse;
import com.example.exceptions.UserNotFoundException;
import com.example.url_profile.UrlProfile;
import com.example.url_profile.UrlProfileMapper;
import com.example.user.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UrlViewServiceTest {

    @Mock
    private UrlViewRepository urlViewRepository;
    @Mock
    private UrlViewMapper urlViewMapper;
    @Mock
    private UrlProfileMapper urlProfileMapper;
    @InjectMocks
    private UrlViewService urlViewService;

    private UrlViewDto urlViewDto;
    private UrlProfileResponse urlProfileResponse;
    private User user;
    private UrlView urlView;
    private UrlProfile urlProfile;


    @BeforeEach
    void setUp(){
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");

        urlViewDto = new UrlViewDto();
        urlViewDto.setBrowser("Chrome");
        urlViewDto.setReferer("www.google.com");
        urlViewDto.setOsSystem("Windows");
        urlViewDto.setIpAddress("1.2.3.4");

        urlProfileResponse = new UrlProfileResponse();
        urlProfileResponse.setId(1L);
        urlProfileResponse.setShortUrl("abc123");
        urlProfileResponse.setLongUrl("http://example.com");
        urlProfileResponse.setCreatedBy(1L);
        urlProfileResponse.setValidTo(LocalDateTime.now().plusMonths(1));

        urlProfile = new UrlProfile();
        urlProfile.setId(1L);
        urlProfile.setShortUrl("abc123");
        urlProfile.setLongUrl("http://example.com");
        urlProfile.setUser(user);
        urlProfile.setValidTo(LocalDateTime.now().plusMonths(1));
        urlProfile.setCreatedAt(LocalDateTime.now());

        urlView = new UrlView();
        urlView.setId(1L);
        urlView.setUrlProfile(urlProfile);
        urlView.setIpAddress("1.2.3.4");
        urlView.setBrowser("Chrome");
        urlView.setOsSystem("Windows");
        urlView.setReferer("www.google.com");
    }

    @Test
    void saveUrlViewTest() throws UserNotFoundException {
        when(urlViewMapper.fromUrlViewDtoToEntity(urlViewDto)).thenReturn(urlView);
        when(urlViewRepository.save(urlView)).thenReturn(urlView);
        when(urlViewMapper.fromUrlViewEntityToResponse(urlView)).thenReturn(new UrlViewResponse(1L, urlProfile, "1.2.3.4", "Windows", "Chrome", "www.google.com"));

        UrlViewResponse response = urlViewService.saveUrlView(urlViewDto, urlProfileResponse);

        assertNotNull(response);
        verify(urlViewRepository, times(1)).save(urlView);
    }

    @Test
    void getViewsForUrlTest() {

    }

    @Test
    void getCountOfRedirectsForUrlTest() {
        when(urlViewRepository.countByUrlId(1L)).thenReturn(1L);

        Assertions.assertEquals(1L, urlViewService.getCountOfRedirectsForUrl(1L));
    }
}