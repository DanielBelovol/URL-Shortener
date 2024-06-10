package com.example.auth.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class PasswordValidatorImpl implements ConstraintValidator<PasswordValidator, String> {

    private static final Pattern PASSWORD_PATTERN = Pattern.
            compile("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])[^ ]{8,64}$");
    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null) {
            return false;
        }
        if (!password.matches(PASSWORD_PATTERN.pattern())) {
            if (context != null) {
                context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                        .addConstraintViolation();
            }
            return false;
        }
        return true;
    }
}
