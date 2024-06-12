package com.example.url_view;

import com.example.role.Role;
import com.example.role.RoleRepository;
import com.example.testcontainers.BaseTestWithPostgresContainer;
import com.example.url_profile.UrlProfile;
import com.example.url_profile.UrlProfileRepository;
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
import java.util.ArrayList;
import java.util.List;

@DataJpaTest
@ActiveProfiles("default")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UrlViewRepositoryTest{
    @Autowired
    private UrlProfileRepository urlProfileRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UrlViewRepository urlViewRepository;

    private User user;
    private UrlProfile urlProfile;
    private Role role;
    private UrlView urlView;

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

        urlProfile = new UrlProfile();
        urlProfile.setLongUrl("https://www.youtube.com/");
        urlProfile.setShortUrl("eXaMpLe");
        urlProfile.setCreatedAt(LocalDateTime.now());
        urlProfile.setValidTo(LocalDateTime.now().plusMonths(1));
        urlProfile.setUser(user);
        urlProfileRepository.save(urlProfile);

        urlView = new UrlView();
        urlView.setIpAddress("1.2.3.4");
        urlView.setBrowser("Chrome");
        urlView.setOsSystem("Windows");
        urlView.setReferer("www.google.com");
        urlView.setUrlProfile(urlProfile);
        urlViewRepository.save(urlView);
    }
    @Test
    void findAllByUrlIdTest() {
        List<UrlView> expectedViews = new ArrayList<>();
        expectedViews.add(urlView);

        List<UrlView> actualViews = urlViewRepository.findAllByUrlId(urlProfile.getId());

        Assertions.assertIterableEquals(expectedViews, actualViews);
    }
    @Test
    void findZeroViewsByUrlIdTest() {
        List<UrlView> expectedViews = new ArrayList<>();

        List<UrlView> actualViews = urlViewRepository.findAllByUrlId(100L);

        Assertions.assertIterableEquals(expectedViews, actualViews);
    }

    @Test
    void countByUrlIdTest() {
        Long expectedResult = 1L;

        Long actualResult = urlViewRepository.countByUrlId(urlProfile.getId());

        Assertions.assertEquals(expectedResult, actualResult);
    }
    @Test
    void countZeroViewsByUrlIdTest() {
        Long expectedResult = 0L;

        Long actualResult = urlViewRepository.countByUrlId(100L);

        Assertions.assertEquals(expectedResult, actualResult);
    }


}