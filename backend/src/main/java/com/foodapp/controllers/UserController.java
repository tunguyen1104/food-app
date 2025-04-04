package com.foodapp.controllers;

import com.foodapp.domain.User;
import com.foodapp.dto.response.ApiResponse;
import com.foodapp.dto.response.UserResponse;
import com.foodapp.dto.response.UserSettingsResponse;
import com.foodapp.mapper.UserMapper;
import com.foodapp.services.Impl.UserService;
import com.foodapp.utils.AuthenticationFacade;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserController {
    UserMapper userMapper;
    AuthenticationFacade authenticationFacade;
    UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile() {
        User currentUser = authenticationFacade.getAuthenticatedUser();

        UserResponse response = userMapper.toUserResponse(currentUser);

        return ResponseEntity.ok(
                ApiResponse.builder()
                        .status(ApiResponse.Status.SUCCESS)
                        .data(response)
                        .build()
        );
    }

    @PutMapping("/settings")
    public ResponseEntity<?> updateSettings(@RequestBody @Valid UserSettingsResponse request) {
        userService.updateUserSettings(request);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .status(ApiResponse.Status.SUCCESS)
                        .build()
        );
    }

    @GetMapping("/settings")
    public ResponseEntity<?> getSettings() {
        UserSettingsResponse response = userService.getUserSettings();
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .status(ApiResponse.Status.SUCCESS)
                        .data(response)
                        .build()
        );
    }
}