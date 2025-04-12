package com.foodapp.services;

import com.foodapp.dto.requests.NotificationRequest;
import com.foodapp.dto.response.NotificationResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface INotificationService {
    List<NotificationResponse> getNotifications(String userId);

    void markAsRead(String notificationId);

    void markAllAsRead(String userId);

    void processNotification(NotificationRequest request);
}
