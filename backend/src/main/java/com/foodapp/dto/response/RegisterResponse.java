package com.foodapp.dto.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class RegisterResponse {
    private String username;
    private String phone;
}
