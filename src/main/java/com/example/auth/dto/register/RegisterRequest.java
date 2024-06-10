package com.example.auth.dto.register;

import com.example.auth.validation.EmailValidator;
import com.example.auth.validation.PasswordValidator;
import lombok.Data;

@Data
public class RegisterRequest {

    private String username;

    @EmailValidator
    private String email;

    @PasswordValidator
    private String password;

}
