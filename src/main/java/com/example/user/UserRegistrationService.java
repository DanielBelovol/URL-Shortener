package com.example.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserRegistrationService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(String login, String password) {
        if (userRepository.existsByLogin(login)) {
            throw new RuntimeException("Login already exists");
        }

        if (password.length() < 8 || !password.matches(".*\\d.*") || !password.matches(".*[a-z].*") || !password.matches(".*[A-Z].*")) {
            throw new RuntimeException("Password must contain at least 8 characters, including digits, uppercase and lowercase letters");
        }

        User user = new User();
        user.setLogin(login);
        user.setPassword(passwordEncoder.encode(password));
        return userRepository.save(user);
    }

    public boolean authenticateUser(String login, String password) {
        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Порівнюємо зашифрований пароль з введеним паролем
        return passwordEncoder.matches(password, user.getPassword());
    }
}
