package com.foodapp.domain.mongo;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(collection = "conversations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Conversation {
    @Id
    private String id;

    private List<String> participantIds;

    private Date createdAt;
    private String lastMessage;
    private Date lastUpdate;
    private String senderAvatarUrl;
    private String receiverAvatarUrl;
    private String receiverName;
}
