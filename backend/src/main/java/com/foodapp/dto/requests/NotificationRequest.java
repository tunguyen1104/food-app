package com.foodapp.dto.requests;

import com.foodapp.domain.mongo.Notification;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationRequest {
    
    @NotBlank
    private String userId;

    @NotBlank
    private String title;

    @NotBlank
    private String message;

    @NotNull
    private Notification.NotificationType type;
}
