package com.example.auth.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class EmailValidatorImpl implements ConstraintValidator<EmailValidator, String> {
    private static final Pattern EMAIL_PATTERN = Pattern.
            compile("^[A-Za-z0-9]+[._+-]?[A-Za-z0-9]+@[A-Za-z0-9]+[._-]?[A-Za-z0-9]+\\.[A-Za-z]{2,}$");

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null) {
            return false;
        }
        if (!email.matches(EMAIL_PATTERN.pattern())) {
            if (context != null) {
                context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                        .addConstraintViolation();
            }
            return false;
        }
        return true;
    }
}
