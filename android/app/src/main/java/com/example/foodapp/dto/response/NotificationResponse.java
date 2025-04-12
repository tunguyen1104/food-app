package com.example.foodapp.dto.response;

import com.example.foodapp.enums.NotificationType;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationResponse {
    private String id;

    private String userId;
    private String title;
    private String message;

    private boolean read;
    private Date timestamp;
    private NotificationType type;
}
