package com.foodapp.services;

import com.foodapp.domain.mongo.Message;
import com.foodapp.dto.requests.ConversationRequest;
import com.foodapp.dto.requests.MessageRequest;
import com.foodapp.dto.response.MessageResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IMessageService {
    List<MessageResponse> getMessagesByConversation(ConversationRequest conversationRequest);

    void updateMessageStatus(String messageId, Message.MessageStatus newStatus);

    void markAllRead(String conversationId, String userId);

    void handleSaveMessage(MessageRequest messageRequest);
}
