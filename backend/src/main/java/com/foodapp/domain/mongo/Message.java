package com.foodapp.domain.mongo;

import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;

@Document(collection = "messages")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message {
    @Id
    private String id;

    private String conversationId;
    private String senderId;
    private String receiverId;

    private String content;
    private Date timestamp;

    private MessageStatus status;

    public enum MessageStatus {
        SENT,
        DELIVERED,
        READ
    }

    @PrePersist
    public void prePersist() {
        if (this.timestamp == null) {
            this.timestamp = new Date();
        }
    }
}
