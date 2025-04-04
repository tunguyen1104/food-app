package com.foodapp.mapper;

import com.foodapp.domain.User;
import com.foodapp.dto.response.UserResponse;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponse toUserResponse(User user) {
        if (user == null) {
            return null;
        }
        return UserResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .phone(user.getPhone())
                .userName(user.getUsername())
                .enabled(user.getEnabled())
                .role(user.getRole().getName())
                .build();
    }
}
