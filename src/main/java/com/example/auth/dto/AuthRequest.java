package com.example.auth.dto;

import com.example.auth.validation.EmailValidator;
import com.example.auth.validation.PasswordValidator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequest {

    @EmailValidator
    private String email;

    @PasswordValidator
    private String password;

}
