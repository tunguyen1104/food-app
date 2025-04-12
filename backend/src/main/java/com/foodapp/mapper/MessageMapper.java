package com.foodapp.mapper;

import com.foodapp.domain.mongo.Message;
import com.foodapp.dto.requests.MessageRequest;
import com.foodapp.dto.response.MessageResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;

@Component
public class MessageMapper {

    public MessageResponse toMessageResponse(Message message) {
        if (message == null) return null;
        return MessageResponse.builder()
                .id(message.getId())
                .content(message.getContent())
                .conversationId(message.getConversationId())
                .senderId(message.getSenderId())
                .receiverId(message.getReceiverId())
                .timestamp(message.getTimestamp())
                .status(message.getStatus())
                .build();
    }

    public Message toMessage(MessageRequest request) {
        if (request == null) {
            return null;
        }

        return Message.builder()
                .content(request.getContent())
                .conversationId(request.getConversationId())
                .senderId(request.getSenderId())
                .receiverId(request.getReceiverId())
                .timestamp(new Date())
                .status(Message.MessageStatus.SENT)
                .build();
    }
}
