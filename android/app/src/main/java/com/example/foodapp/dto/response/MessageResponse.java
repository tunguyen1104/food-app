package com.example.foodapp.dto.response;

import com.example.foodapp.enums.MessageStatus;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
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
    private MessageStatus status;
}
