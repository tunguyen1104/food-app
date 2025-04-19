package com.example.foodapp.listeners;

import com.example.foodapp.dto.response.NotificationResponse;

public interface NotificationListener {
    void onNotificationReceived(NotificationResponse notification);
}
