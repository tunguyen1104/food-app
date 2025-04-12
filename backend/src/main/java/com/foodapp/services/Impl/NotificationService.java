package com.foodapp.services.Impl;

import com.foodapp.domain.mongo.Notification;
import com.foodapp.dto.requests.NotificationRequest;
import com.foodapp.dto.response.NotificationResponse;
import com.foodapp.mapper.NotificationMapper;
import com.foodapp.repositories.NotificationRepository;
import com.foodapp.services.INotificationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotificationService implements INotificationService {

    NotificationRepository notificationRepository;
    NotificationMapper notificationMapper;
    SimpMessagingTemplate messagingTemplate;

    @Override
    public List<NotificationResponse> getNotifications(String userId) {
        List<Notification> notificationList = notificationRepository.findByUserIdOrderByTimestampDesc(userId);
        return notificationList.stream()
                .map(notificationMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void markAsRead(String notificationId) {
        Notification notif = notificationRepository.findById(notificationId).orElse(null);
        if (notif != null && !notif.isRead()) {
            notif.setRead(true);
            notificationRepository.save(notif);
        }
    }

    @Override
    public void markAllAsRead(String userId) {
        List<Notification> unreadNotifications = notificationRepository.findByUserIdAndReadIsFalse(userId);

        if (unreadNotifications.isEmpty()) {
            return;
        }

        for (Notification notification : unreadNotifications) {
            notification.setRead(true);
        }

        notificationRepository.saveAll(unreadNotifications);
    }

    @Override
    public void processNotification(NotificationRequest request) {
        Notification notification = Notification.builder()
                .userId(request.getUserId())
                .title(request.getTitle())
                .message(request.getMessage())
                .type(request.getType())
                .read(false)
                .timestamp(new Date())
                .build();

        Notification saved = notificationRepository.save(notification);

        messagingTemplate.convertAndSend(
                "/topic/notifications/" + saved.getUserId(),
                saved
        );
    }
}
