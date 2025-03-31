package com.foodapp.controllers;

import com.foodapp.dto.response.ApiResponse;
import com.foodapp.services.Impl.UserService;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserController {
    UserService userService;

    @PutMapping("/{id}/enable")
    public ResponseEntity<?> enableEmployee(@PathVariable @NotNull Long id) {
        userService.enableEmployeeAccount(id);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .status("Success")
                        .build());
    }
}
