package com.example.user;

import com.example.exceptions.UserNotFoundException;
import com.example.role.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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
    public void deleteUserById(Long userId){
        userRepository.deleteById(userId);
    }

}
