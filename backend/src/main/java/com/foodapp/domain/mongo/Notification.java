package com.foodapp.domain.mongo;

import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "notifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {
    @Id
    private String id;

    private String userId;
    private String title;
    private String message;

    private boolean read;
    private Date timestamp;
    private NotificationType type;

    public enum NotificationType {
        MESSAGE, ORDER, CHECKOUT;
    }

    @PrePersist
    public void prePersist() {
        if (this.timestamp == null) {
            this.timestamp = new Date();
        }
    }
}
