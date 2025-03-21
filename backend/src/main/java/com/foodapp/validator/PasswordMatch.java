package com.foodapp.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = PasswordMatchValidator.class)
@Target(ElementType.TYPE) // Applies to the whole class
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordMatch {
    String message() default "Passwords do not match";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}