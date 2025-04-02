package com.foodapp.dto.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginRequest {
    @NotBlank(message = "Phone number must not be blank")
    @Pattern(regexp = "^0[3-9][0-9]{8}", message = "Phone number is not valid")
    private String phone;
    @NotBlank(message = "Password must not be blank")
    @Size(min = 6, max = 64)
    private String password;
}
