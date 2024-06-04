package com.example.auth.service;

import com.example.auth.dto.AuthRequest;
import com.example.auth.exception.EmailAlreadyUsedException;
import com.example.auth.jwt.JwtUtil;
import com.example.user.User;
import com.example.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public String loginUser(AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getEmail(), authRequest.getPassword()));

        return jwtUtil.generateToken(authentication);
    }

    public String registerUser(AuthRequest authRequest) {
        String email = authRequest.getEmail();

        if (userRepository.findByEmail(email).isPresent()) {
            throw new EmailAlreadyUsedException(email);
        }
        saveNewUser(authRequest);
        return loginUser(authRequest);
    }

    private void saveNewUser(AuthRequest authRequest) {
        User user = new User();
        user.setEmail(authRequest.getEmail());
        user.setPassword(passwordEncoder.encode(authRequest.getPassword()));

        //Need enum of roles (Admin, User)
        //user.setRole(Role.USER);

        userRepository.save(user);
    }

}
