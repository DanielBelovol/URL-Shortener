package com.example.user;

import org.springframework.beans.factory.annotation.Autowired;

public class UserService {
    @Autowired
    private UserRepository userRepository;

    public boolean existsByLogin(String login){
        return userRepository.existsByLogin(login);
    }
}
