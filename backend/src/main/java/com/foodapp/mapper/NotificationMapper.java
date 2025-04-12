package com.foodapp.mapper;

import com.foodapp.domain.mongo.Notification;
import com.foodapp.dto.response.NotificationResponse;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {
    public NotificationResponse toResponse(Notification notif) {
        return NotificationResponse.builder()
                .id(notif.getId())
                .userId(notif.getUserId())
                .title(notif.getTitle())
                .message(notif.getMessage())
                .read(notif.isRead())
                .timestamp(notif.getTimestamp())
                .type(notif.getType())
                .build();
    }
}
