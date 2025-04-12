package com.foodapp.mapper;

import com.foodapp.domain.mongo.Conversation;
import com.foodapp.dto.response.ConversationResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ConversationMapper {

    public ConversationResponse toConversationResponse(Conversation conversation) {
        if (conversation == null) return null;
        return ConversationResponse.builder()
                .id(conversation.getId())
                .participantIds(conversation.getParticipantIds())
                .createdAt(conversation.getCreatedAt())
                .lastMessage(conversation.getLastMessage())
                .lastUpdate(conversation.getLastUpdate())
                .senderAvatarUrl(conversation.getSenderAvatarUrl())
                .receiverAvatarUrl(conversation.getReceiverAvatarUrl())
                .receiverName(conversation.getReceiverName())
                .build();
    }

    public List<ConversationResponse> toConversationResponseList(List<Conversation> conversations) {
        return conversations.stream()
                .map(this::toConversationResponse)
                .collect(Collectors.toList());
    }

}
