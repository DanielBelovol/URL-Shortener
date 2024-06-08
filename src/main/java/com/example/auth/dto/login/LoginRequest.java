package com.example.auth.dto.login;

import com.example.auth.validation.EmailValidator;
import com.example.auth.validation.PasswordValidator;
import lombok.Data;

@Data
public class LoginRequest {

    @EmailValidator
    private String email;

    @PasswordValidator
    private String password;

}
