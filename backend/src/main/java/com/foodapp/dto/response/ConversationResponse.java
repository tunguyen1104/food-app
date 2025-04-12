package com.foodapp.dto.response;

import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConversationResponse implements Serializable {
    private String id;
    private List<String> participantIds;
    private Date createdAt;
    private String lastMessage;
    private Date lastUpdate;
    private String senderAvatarUrl;
    private String receiverAvatarUrl;
    private String receiverName;
}
