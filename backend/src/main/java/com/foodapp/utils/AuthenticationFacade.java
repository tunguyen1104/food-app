package com.foodapp.utils;

import com.foodapp.constants.ErrorCode;
import com.foodapp.domain.User;
import com.foodapp.exceptions.AppException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationFacade {

    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof UsernamePasswordAuthenticationToken authenticatedUser)) {
            throw new AppException(ErrorCode.ACCESS_DENIED);
        }
        return (User) authenticatedUser.getPrincipal();
    }
}
