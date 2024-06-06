package com.example.auth.dto.register;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterResponse {

    private Error error;

    private String authToken;

    private String message;

    public enum Error {
        ok,
        usernameAlreadyExist,
        emailAlreadyExist
    }

    public static RegisterResponse success(String authToken, String message) {
        return builder()
                .message(message)
                .error(Error.ok)
                .authToken(authToken)
                .build();
    }

    public static RegisterResponse failed(Error error) {
        return builder()
                .error(error)
                .build();
    }

}
