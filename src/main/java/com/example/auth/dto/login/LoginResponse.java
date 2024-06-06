package com.example.auth.dto.login;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {

    private Error error;

    private String authToken;

    private String message;

    public enum Error {
        ok,
        invalidEmail,
        invalidPassword
    }

    public static LoginResponse success(String authToken, String message) {
        return builder()
                .message(message)
                .error(Error.ok)
                .authToken(authToken)
                .build();
    }

    public static LoginResponse failed(Error error) {
        return builder()
                .error(error)
                .build();
    }

}
