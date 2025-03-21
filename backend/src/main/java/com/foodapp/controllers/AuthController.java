package com.foodapp.controllers;

import com.foodapp.dto.requests.RefreshTokenRequest;
import com.foodapp.dto.requests.UserLoginRequest;
import com.foodapp.dto.requests.UserLogoutRequest;
import com.foodapp.dto.requests.UserRegistrationRequest;
import com.foodapp.dto.response.ApiResponse;
import com.foodapp.services.IAuthenticationService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {
    IAuthenticationService authenticationService;
    Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/signIn")
    public ResponseEntity<?> login(@Valid @RequestBody UserLoginRequest request) {
        logger.info("Login request received");
        var result = authenticationService.authenticate(request.getPhone(), request.getPassword());
        return ResponseEntity.ok(ApiResponse.builder()
                .data(result)
                .status("Success")
                .build());
    }

    @PostMapping("/signUp")
    public ResponseEntity<?> register(@Valid @RequestBody UserRegistrationRequest request) {
        var result = authenticationService.register(request);
        return ResponseEntity.ok(ApiResponse.builder()
                .data(result)
                .status("Success")
                .build());
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        logger.info("Refresh token request: {}", request);
        var result = authenticationService.refreshToken(request.getRefreshToken());
        return ResponseEntity.ok(ApiResponse.builder()
                .data(result)
                .status("Success")
                .build());
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@Valid @RequestBody UserLogoutRequest request) {
        logger.info("Logout request: {}", request);
        var result = authenticationService.logout(request);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .data(result)
        );
    }
}
