package com.foodapp.validator;


import com.foodapp.dto.requests.UserRegistrationRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchValidator implements ConstraintValidator<PasswordMatch,
        UserRegistrationRequest> {

    @Override
    public boolean isValid(UserRegistrationRequest request, ConstraintValidatorContext context) {
        if (request.getPassword() == null || request.getConfirmPassword() == null) {
            return false; // Passwords cannot be null
        }
        boolean isValid = request.getPassword().equals(request.getConfirmPassword());

        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Passwords do not match")
                    .addPropertyNode("confirmPassword")
                    .addConstraintViolation();
        }
        return isValid;
    }
}

