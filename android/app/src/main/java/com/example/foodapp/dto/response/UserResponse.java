package com.example.foodapp.dto.response;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse implements Serializable {
    private Long id;
    private String fullName;
    private String phone;
    private String userName;
    private Boolean enabled;
    private String role;
    private String avatarUrl;
    private String location;
}
