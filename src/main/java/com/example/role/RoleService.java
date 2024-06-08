package com.example.role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    public RoleOld findRoleByUsername(String username){
        return roleRepository.findByName(username);
    }
}
