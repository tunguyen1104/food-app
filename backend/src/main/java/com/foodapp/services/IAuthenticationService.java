package com.foodapp.services;

import com.foodapp.dto.requests.UserLogoutRequest;
import com.foodapp.dto.requests.UserRegistrationRequest;
import com.foodapp.dto.response.RegisterResponse;
import com.foodapp.dto.response.TokenResponse;
import org.springframework.stereotype.Service;

@Service
public interface IAuthenticationService {
    TokenResponse authenticate(String email, String password);

    RegisterResponse register(UserRegistrationRequest request);

    TokenResponse refreshToken(String refreshToken);

    Boolean logout(UserLogoutRequest request);
}