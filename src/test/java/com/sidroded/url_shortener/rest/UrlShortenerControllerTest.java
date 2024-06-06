package com.sidroded.url_shortener.rest;

import com.sidroded.url_shortener.url_profile.UrlProfile;
import com.sidroded.url_shortener.url_profile.UrlProfileDTO;
import com.sidroded.url_shortener.url_profile.UrlProfileRequest;
import com.sidroded.url_shortener.url_profile.UrlProfileService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UrlShortenerController.class)
public class UrlShortenerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UrlProfileService urlProfileService;

    @Test
    @WithMockUser(username = "user", password = "default")
    public void getUrlProfileTest() throws Exception {
        UrlProfileDTO urlProfileDTO = new UrlProfileDTO(
                1L,
                "http://example.com",
                "shortUrl",
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1),
                0,
                1L
        );

        when(urlProfileService.getById(1L)).thenReturn(urlProfileDTO);

        mockMvc.perform(get("/v1/url_profile/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.fullUrl").value("http://example.com"))
                .andExpect(jsonPath("$.shortUrl").value("shortUrl"))
                .andExpect(jsonPath("$.views").value(0))
                .andExpect(jsonPath("$.userId").value(1L));
    }

    @Test
    @WithMockUser(username = "user", password = "default")
    public void getAllUrlProfilesTest() throws Exception {
        UrlProfile urlProfile = new UrlProfile("http://example.com", 1L);
        when(urlProfileService.getAll()).thenReturn(Arrays.asList(urlProfile));

        mockMvc.perform(get("/v1/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].fullUrl").value("http://example.com"));
    }

    @Test
    @WithMockUser(username = "user", password = "default")
    public void addUrlProfileTest() throws Exception {
        UrlProfileRequest urlProfileRequest = new UrlProfileRequest();
        urlProfileRequest.setFullUrl("http://example.com");
        urlProfileRequest.setUserId(1L);

        UrlProfile urlProfile = new UrlProfile("http://example.com", 1L);

        when(urlProfileService.save(any(UrlProfile.class))).thenReturn(urlProfile);

        mockMvc.perform(post("/v1/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"fullUrl\": \"http://example.com\", \"userId\": 1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullUrl").value("http://example.com"));
    }

    @Test
    @WithMockUser(username = "user", password = "default")
    public void redirectToFullUrlTest() throws Exception {
        UrlProfile urlProfile = new UrlProfile("http://example.com", 1L);
        urlProfile.setShortUrl("shortUrl");

        when(urlProfileService.getByShortUrl("shortUrl")).thenReturn(urlProfile);

        mockMvc.perform(get("/v1/shortUrl"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://example.com"));
    }

    @Test
    @WithMockUser(username = "user", password = "default")
    public void deleteByIdTest() throws Exception {
        Mockito.doNothing().when(urlProfileService).deleteByShortUrl("shortUrl");

        mockMvc.perform(post("/v1/shortUrl"))
                .andExpect(status().isOk());
    }
}