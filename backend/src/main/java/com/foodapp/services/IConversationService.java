package com.foodapp.services;

import com.foodapp.domain.mongo.Conversation;
import com.foodapp.dto.response.ConversationResponse;
import com.foodapp.dto.response.MessageResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IConversationService {
    ConversationResponse createOrFindConversation(List<String> participantIds);

    Conversation createNewConversation(List<String> participantIds);

    String findConversation(List<String> participantIds);

    List<ConversationResponse> getAllConversationForAdmin(String adminId);

    void updateLastMessage(MessageResponse messageResponse);
}
