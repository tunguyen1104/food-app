package com.foodapp.dto.response;

import com.foodapp.domain.mongo.Notification;
import lombok.*;

import java.util.Date;

@Getter
@Setter
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
    private Notification.NotificationType type;
}
