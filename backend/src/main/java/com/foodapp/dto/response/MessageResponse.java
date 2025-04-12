package com.foodapp.dto.response;

import com.foodapp.domain.mongo.Message;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageResponse implements Serializable {
    private String id;
    private String conversationId;
    private String senderId;
    private String receiverId;
    private String content;
    private Date timestamp;
    private Message.MessageStatus status;
}
