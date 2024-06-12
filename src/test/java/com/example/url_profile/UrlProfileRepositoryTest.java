package com.example.url_profile;

import com.example.role.Role;
import com.example.role.RoleRepository;
import com.example.testcontainers.BaseTestWithPostgresContainer;
import com.example.user.User;
import com.example.user.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest
@ActiveProfiles("default")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UrlProfileRepositoryTest extends BaseTestWithPostgresContainer {

    @Autowired
    private UrlProfileRepository urlProfileRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    private User user;
    private UrlProfile expectedUrl;
    private Role role;

    @BeforeEach
    void setUp() {
        role = new Role();
        role.setName("USER");
        roleRepository.save(role);

        user = new User();
        user.setId(1L);
        user.setUsername("user");
        user.setEmail("user@example.com");
        user.setPassword("Qazwsx0123");
        user.setRole(role);
        user.setCreatedAt(LocalDateTime.now());
        userRepository.save(user);

        expectedUrl = new UrlProfile();
        expectedUrl.setLongUrl("https://www.youtube.com/");
        expectedUrl.setShortUrl("eXaMpLe");
        expectedUrl.setCreatedAt(LocalDateTime.now());
        expectedUrl.setValidTo(LocalDateTime.now().plusMonths(1));
        expectedUrl.setUser(user);

        urlProfileRepository.save(expectedUrl);
    }

    @Test
    void findByShortUrlTest() {
        UrlProfile actualUrl = urlProfileRepository.findByShortUrl("eXaMpLe");
        Assertions.assertEquals(expectedUrl, actualUrl);
    }

    @Test
    void deleteUrlProfileByShortUrlTest() {
        urlProfileRepository.deleteUrlProfileByShortUrl("eXaMpLe");
        Assertions.assertFalse(urlProfileRepository.existsByShortUrl("eXaMpLe"));
    }

    @Test
    void existsByShortUrlTest() {
        Assertions.assertTrue(urlProfileRepository.existsByShortUrl("eXaMpLe"));
        Assertions.assertFalse(urlProfileRepository.existsByShortUrl("NonExistingUrl"));
    }

    @Test
    void getAllActiveUrlsTest() {
        List<UrlProfile> activeUrls = urlProfileRepository.getAllActiveUrls();
        Assertions.assertFalse(activeUrls.isEmpty());
        Assertions.assertTrue(activeUrls.contains(expectedUrl));
    }
}