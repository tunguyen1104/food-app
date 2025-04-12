package com.foodapp.services.Impl;

import com.foodapp.constants.ErrorCode;
import com.foodapp.domain.mongo.Conversation;
import com.foodapp.domain.User;
import com.foodapp.dto.response.ConversationResponse;
import com.foodapp.dto.response.MessageResponse;
import com.foodapp.exceptions.AppException;
import com.foodapp.mapper.ConversationMapper;
import com.foodapp.repositories.ConversationRepository;
import com.foodapp.repositories.UserRepository;
import com.foodapp.services.IConversationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ConversationService implements IConversationService {

    ConversationRepository conversationRepository;
    ConversationMapper conversationMapper;
    UserRepository userRepository;

    @Override
    public ConversationResponse createOrFindConversation(List<String> participantIds) {

        validateParticipantIds(participantIds);

        sortParticipantIds(participantIds);

        Optional<Conversation> existingConversation = conversationRepository
                .findByParticipantIdsAllAndSize(participantIds, participantIds.size());

        if (existingConversation.isPresent()) {
            return conversationMapper.toConversationResponse(existingConversation.get());
        }

        Conversation newConversation = createNewConversation(participantIds);
        return conversationMapper.toConversationResponse(newConversation);
    }

    private void validateParticipantIds(List<String> participantIds) {
        if (participantIds == null || participantIds.size() < 2) {
            throw new AppException(ErrorCode.CONVERSATION_NOT_FOUND);
        }
    }

    private void sortParticipantIds(List<String> participantIds) {
        participantIds.sort(String::compareTo);
    }

    @Override
    public Conversation createNewConversation(List<String> participantIds) {
        User sender = userRepository.findById(
                        Long.parseLong(participantIds.get(0)))
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        User receiver = userRepository.findById(
                        Long.parseLong(participantIds.get(1)))
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Conversation newConversation = Conversation.builder()
                .participantIds(participantIds)
                .createdAt(new Date())
                .senderAvatarUrl(sender.getAvatarUrl())
                .receiverAvatarUrl(receiver.getAvatarUrl())
                .receiverName(receiver.getFullName())
                .build();
        conversationRepository.save(newConversation);
        return newConversation;
    }

    @Override
    public List<ConversationResponse> getAllConversationForAdmin(String adminId) {
        List<Conversation> conversations = conversationRepository.findAllByParticipantIds(adminId);
        if (conversations == null || conversations.isEmpty()) {
            throw new AppException(ErrorCode.CONVERSATION_NOT_FOUND);
        }
        return conversationMapper.toConversationResponseList(conversations);
    }

    @Override
    public void updateLastMessage(MessageResponse messageResponse) {
        String conversationId = messageResponse.getConversationId();
        if (conversationId == null) return;

        conversationRepository.findById(conversationId).ifPresent(conversation -> {
            conversation.setLastMessage(messageResponse.getContent());
            conversation.setLastUpdate(new Date());
            conversationRepository.save(conversation);
        });
    }
}
