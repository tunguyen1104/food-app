package com.foodapp.services.Impl;

import com.foodapp.domain.mongo.Message;
import com.foodapp.dto.requests.ConversationRequest;
import com.foodapp.dto.requests.MessageRequest;
import com.foodapp.dto.response.ConversationResponse;
import com.foodapp.dto.response.MessageResponse;
import com.foodapp.mapper.MessageMapper;
import com.foodapp.repositories.MessageRepository;
import com.foodapp.services.IConversationService;
import com.foodapp.services.IMessageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MessageService implements IMessageService {
    MessageRepository messageRepository;
    MessageMapper messageMapper;
    SimpMessagingTemplate messagingTemplate;
    IConversationService conversationService;

    @Override
    public List<MessageResponse> getMessagesByConversation(ConversationRequest conversationRequest) {
        String conversationId = conversationService.findConversation(conversationRequest.getParticipantIds());

        List<Message> messages = messageRepository.findByConversationIdOrderByTimestampAsc(conversationId);

        return messages.stream()
                .map(messageMapper::toMessageResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void updateMessageStatus(String messageId, Message.MessageStatus newStatus) {
        Optional<Message> opt = messageRepository.findById(messageId);

        if (opt.isPresent()) {
            Message msg = opt.get();
            msg.setStatus(newStatus);
            messageRepository.save(msg);
        }
    }

    @Override
    public void markAllRead(String conversationId, String userId) {
        List<Message> messages = messageRepository.findByConversationIdOrderByTimestampAsc(conversationId);
        for (Message m : messages) {
            if (m.getReceiverId().equals(userId) && m.getStatus() != Message.MessageStatus.READ) {
                m.setStatus(Message.MessageStatus.READ);
            }
        }
        messageRepository.saveAll(messages);
    }

    @Override
    public void handleSaveMessage(MessageRequest messageRequest) {

        if (messageRequest.getConversationId() == null) {
            List<String> participantIds = Arrays.asList(messageRequest.getSenderId(), messageRequest.getReceiverId());
            String conversations = conversationService.findConversation(participantIds);
            messageRequest.setConversationId(conversations);
        }

        Message messageEntity = messageMapper.toMessage(messageRequest);

        Message savedMessage = messageRepository.save(messageEntity);

        MessageResponse saved = messageMapper.toMessageResponse(savedMessage);
        conversationService.updateLastMessage(saved);
        messagingTemplate.convertAndSend("/topic/messages/" + saved.getReceiverId(), saved);
    }

}
