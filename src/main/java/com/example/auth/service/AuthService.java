package com.example.auth.service;

import com.example.auth.dto.login.LoginRequest;
import com.example.auth.dto.login.LoginResponse;
import com.example.auth.dto.register.RegisterRequest;
import com.example.auth.dto.register.RegisterResponse;
import com.example.auth.jwt.JwtUtil;
import com.example.role.Role;
import com.example.role.RoleService;
import com.example.user.User;
import com.example.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    private static final String REGISTRATION_MESSAGE = "User registered successfully!";
    private static final String LOGIN_MESSAGE = "User logged in successfully!";

    public RegisterResponse register(RegisterRequest request) {
        Optional<User> userByEmail = userRepository.findByEmail(request.getEmail());

        if (userByEmail.isPresent()) {
            return RegisterResponse.failed(RegisterResponse.Error.emailAlreadyExist);
        }

        Optional<User> userByUsername = userRepository.findByUsername(request.getUsername());

        if (userByUsername.isPresent()) {
            return RegisterResponse.failed(RegisterResponse.Error.usernameAlreadyExist);
        }

        saveNewUser(request);

        String authToken = jwtUtil.generateToken(request.getEmail());

        return RegisterResponse.success(authToken, REGISTRATION_MESSAGE);
    }

    public LoginResponse login(LoginRequest request) {

        Optional<User> user = userRepository.findByEmail(request.getEmail());

        if (Objects.isNull(user)) {
            return LoginResponse.failed(LoginResponse.Error.invalidEmail);
        }

        if (!passwordEncoder.matches(request.getPassword(), user.get().getPassword())) {
            return LoginResponse.failed(LoginResponse.Error.invalidPassword);
        }

        String authToken = jwtUtil.generateToken(request.getEmail());

        return LoginResponse.success(authToken, LOGIN_MESSAGE);
    }

    private void saveNewUser(RegisterRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setCreatedAt(LocalDateTime.now());
        user.setActive(true);
        user.setLocked(false);
        Role userRole = roleService.findRoleByUsername("ROLE_USER");
        user.setRole(userRole);
        userRepository.save(user);
    }

}
