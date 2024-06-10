package com.example.auth.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Constraint(validatedBy = PasswordValidatorImpl.class)
public @interface PasswordValidator {

    String message() default "Password must be at least 8 characters long, " +
            "Password must be at least 8 characters long, contains at least one digit, " +
            "one uppercase letter, one lowercase letter. No spaces are allowed.";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
