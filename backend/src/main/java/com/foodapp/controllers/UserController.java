package com.foodapp.controllers;

import com.foodapp.domain.User;
import com.foodapp.dto.response.ApiResponse;
import com.foodapp.dto.response.UserResponse;
import com.foodapp.mapper.UserMapper;
import com.foodapp.utils.AuthenticationFacade;
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

}
