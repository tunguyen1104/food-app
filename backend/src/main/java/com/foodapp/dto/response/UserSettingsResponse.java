package com.foodapp.dto.response;

import com.foodapp.domain.User;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSettingsResponse {
    private User.Theme theme;
    private Boolean notification;
}