package com.foodapp.controllers;

import com.foodapp.dto.requests.NotificationRequest;
import com.foodapp.dto.response.ApiResponse;
import com.foodapp.services.INotificationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/notifications")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class NotificationController {

    INotificationService notificationService;

    @MessageMapping("/notify")
    public void sendNotification(@Payload NotificationRequest request) {
        this.notificationService.processNotification(request);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getNotifications(@PathVariable String userId) {
        var notifications = notificationService.getNotifications(userId);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .status(ApiResponse.Status.SUCCESS)
                        .data(notifications)
                        .build()
        );
    }

    @PostMapping("/read/{notificationId}")
    public ResponseEntity<?> markAsRead(@PathVariable String notificationId) {
        notificationService.markAsRead(notificationId);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .status(ApiResponse.Status.SUCCESS)
                        .message("Notification marked as read")
                        .build()
        );
    }

    @PostMapping("/read-all/{userId}")
    public ResponseEntity<?> markAllAsRead(@PathVariable String userId) {
        notificationService.markAllAsRead(userId);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .status(ApiResponse.Status.SUCCESS)
                        .message("All notifications marked as read")
                        .build()
        );
    }

}