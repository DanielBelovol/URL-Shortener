package com.example.user;

import com.example.exceptions.UserNotFoundException;
import com.example.role.Role;
import com.example.role.RoleService;
import com.example.url_profile.UrlProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.regex.Pattern;

// todo
// add mapping, dtos

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;

    public User getUserById(long id) throws UserNotFoundException{
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }
    public User getUserByUsername(String username){
        return userRepository.findByUsername(username).orElseThrow(()->new UsernameNotFoundException(username));
    }
    public User getUserByEmail(String userEmail){
        return userRepository.findByEmail(userEmail).orElseThrow(()->new UsernameNotFoundException(userEmail));
    }

}
