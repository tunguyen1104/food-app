package com.foodapp.controllers;

import com.foodapp.domain.User;
import com.foodapp.dto.requests.CreateUserRequest;
import com.foodapp.dto.response.ApiResponse;
import com.foodapp.dto.response.UserResponse;
import com.foodapp.dto.response.UserSettingsResponse;
import com.foodapp.mapper.UserMapper;
import com.foodapp.services.Impl.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@PreAuthorize("hasRole('MANAGER')")
@RestController
@RequestMapping("/api/admin/users")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AdminUserController {
    UserService userService;
    UserMapper userMapper;

    @PutMapping("/{id}/enable")
    public ResponseEntity<?> enableEmployee(@PathVariable @NotNull Long id) {
        userService.enableEmployeeAccount(id);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .status(ApiResponse.Status.SUCCESS)
                        .build());
    }

    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        var users = userService.getAllUsers();
        var userResponses = users.stream()
                .map(userMapper::toUserResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .status(ApiResponse.Status.SUCCESS)
                        .data(userResponses)
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        User user = userService.findByIdOrThrow(id);

        UserResponse response = userMapper.toUserResponse(user);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .status(ApiResponse.Status.SUCCESS)
                        .data(response)
                        .build()
        );
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody @Valid CreateUserRequest request) {
        UserResponse userResponse = userService.createUser(request);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .status(ApiResponse.Status.SUCCESS)
                        .data(userResponse)
                        .build()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable @NotNull Long id, @RequestBody @Valid CreateUserRequest request) {
        UserResponse userResponse = userService.updateUser(id, request);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .status(ApiResponse.Status.SUCCESS)
                        .data(userResponse)
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable @NotNull Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .status(ApiResponse.Status.SUCCESS)
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
