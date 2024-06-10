package com.example.auth.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Constraint(validatedBy = EmailValidatorImpl.class)
public @interface EmailValidator {
    String message() default "Email address entered incorrectly!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
