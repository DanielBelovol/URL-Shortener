package com.sidroded.url_shortener.url_profile;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UrlProfileServiceTest {

    @Mock
    private UrlProfileRepository urlProfileRepository;

    @InjectMocks
    private UrlProfileService urlProfileService;

    @Test
    public void testSaveUrlProfile() {
        UrlProfile urlProfile = new UrlProfile("http://example.com", 1L);
        when(urlProfileRepository.save(urlProfile)).thenReturn(urlProfile);

        UrlProfile savedProfile = urlProfileService.save(urlProfile);

        assertEquals(urlProfile, savedProfile);
        verify(urlProfileRepository, times(1)).save(urlProfile);
    }

    @Test
    public void testGetAllUrlProfiles() {
        UrlProfile urlProfile1 = new UrlProfile("http://example.com/1", 1L);
        UrlProfile urlProfile2 = new UrlProfile("http://example.com/2", 2L);
        when(urlProfileRepository.findAll()).thenReturn(Arrays.asList(urlProfile1, urlProfile2));

        List<UrlProfile> profiles = urlProfileService.getAll();

        assertEquals(2, profiles.size());
        verify(urlProfileRepository, times(1)).findAll();
    }

    @Test
    public void testGetUrlProfileById() {
        Long id = 1L;
        UrlProfile urlProfile = new UrlProfile("http://example.com", 1L);
        when(urlProfileRepository.getReferenceById(id)).thenReturn(urlProfile);

        UrlProfileDTO profileDTO = urlProfileService.getById(id);

        assertEquals(urlProfile.getId(), profileDTO.getId());
        assertEquals(urlProfile.getFullUrl(), profileDTO.getFullUrl());
        verify(urlProfileRepository, times(1)).getReferenceById(id);
    }

    @Test
    public void testGetUrlProfileByShortUrl() {
        String shortUrl = "shortUrl";
        UrlProfile urlProfile = new UrlProfile("http://example.com", 1L);
        when(urlProfileRepository.findByShortUrl(shortUrl)).thenReturn(urlProfile);

        UrlProfile foundProfile = urlProfileService.getByShortUrl(shortUrl);

        assertEquals(urlProfile, foundProfile);
        verify(urlProfileRepository, times(1)).findByShortUrl(shortUrl);
    }

    @Test
    public void testDeleteUrlProfileByShortUrl() {
        String shortUrl = "shortUrl";
        doNothing().when(urlProfileRepository).deleteUrlProfileByShortUrl(shortUrl);

        urlProfileService.deleteByShortUrl(shortUrl);

        verify(urlProfileRepository, times(1)).deleteUrlProfileByShortUrl(shortUrl);
    }
}