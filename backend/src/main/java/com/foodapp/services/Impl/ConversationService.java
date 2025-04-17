package com.foodapp.services.Impl;

import com.foodapp.constants.ErrorCode;
import com.foodapp.domain.User;
import com.foodapp.domain.mongo.Conversation;
import com.foodapp.domain.mongo.Message;
import com.foodapp.dto.response.ConversationResponse;
import com.foodapp.dto.response.MessageResponse;
import com.foodapp.exceptions.AppException;
import com.foodapp.repositories.ConversationRepository;
import com.foodapp.repositories.MessageRepository;
import com.foodapp.repositories.UserRepository;
import com.foodapp.services.IConversationService;
import com.foodapp.utils.AuthenticationFacade;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ConversationService implements IConversationService {

    ConversationRepository conversationRepository;
    MessageRepository messageRepository;
    UserRepository userRepository;
    AuthenticationFacade authenticationFacade;

    @Override
    public ConversationResponse createOrFindConversation(List<String> participantIds) {

        validateParticipantIds(participantIds);

        sortParticipantIds(participantIds);

        Conversation conversation = conversationRepository
                .findByParticipantIdsAllAndSize(participantIds, participantIds.size())
                .stream()
                .findFirst()
                .orElseGet(() -> createNewConversation(participantIds));

        Optional<Message> lastMessageOpt = Optional.ofNullable(conversation.getLastMessageId())
                .flatMap(messageRepository::findById);

        String currentUserId = authenticationFacade.getAuthenticatedUser().getId().toString();
        String receiverId = participantIds.stream()
                .filter(id -> !id.equals(currentUserId))
                .findFirst()
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Optional<User> userReceiverOpt = userRepository.findById(Long.parseLong(receiverId));

        return ConversationResponse.builder()
                .id(conversation.getId())
                .participantIds(conversation.getParticipantIds())
                .createdAt(conversation.getCreatedAt())
                .lastMessage(lastMessageOpt.map(Message::getContent).orElse(null))
                .lastUpdate(lastMessageOpt.map(Message::getTimestamp).orElse(null))
                .receiverAvatarUrl(userReceiverOpt.map(User::getAvatarUrl).orElse(null))
                .receiverName(userReceiverOpt.map(User::getFullName).orElse(null))
                .build();
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
        validateParticipantIds(participantIds);
        sortParticipantIds(participantIds);
        Conversation newConversation = Conversation.builder()
                .participantIds(participantIds)
                .lastMessageId(null)
                .createdAt(new Date())
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
        return conversations.stream().map(conversation -> {

            Optional<Message> lastMessageOpt = Optional.ofNullable(conversation.getLastMessageId())
                    .flatMap(messageRepository::findById);

            Optional<String> receiverIdOpt = conversation.getParticipantIds().stream()
                    .filter(pid -> !pid.equals(adminId))
                    .findFirst();

            Optional<User> userReceiverOpt = receiverIdOpt.flatMap(rid -> {
                try {
                    return userRepository.findById(Long.parseLong(rid));
                } catch (NumberFormatException e) {
                    return Optional.<User>empty();
                }
            });

            return ConversationResponse.builder()
                    .id(conversation.getId())
                    .participantIds(conversation.getParticipantIds())
                    .createdAt(conversation.getCreatedAt())
                    .lastMessage(lastMessageOpt.map(Message::getContent).orElse(null))
                    .lastUpdate(lastMessageOpt.map(Message::getTimestamp).orElse(null))
                    .receiverAvatarUrl(userReceiverOpt.map(User::getAvatarUrl).orElse(null))
                    .receiverName(userReceiverOpt.map(User::getFullName).orElse(null))
                    .build();

        }).collect(Collectors.toList());
    }

    @Override
    public void updateLastMessage(MessageResponse messageResponse) {
        String conversationId = messageResponse.getConversationId();
        if (conversationId == null) throw new AppException(ErrorCode.CONVERSATION_NOT_FOUND);

        conversationRepository.findById(conversationId).ifPresent(conversation -> {
            conversation.setLastMessageId(messageResponse.getId());
            conversationRepository.save(conversation);
        });
    }
}
