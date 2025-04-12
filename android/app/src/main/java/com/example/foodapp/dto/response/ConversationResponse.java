package com.example.foodapp.dto.response;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
