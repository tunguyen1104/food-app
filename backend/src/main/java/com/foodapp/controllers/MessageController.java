package com.foodapp.controllers;

import com.foodapp.dto.requests.ConversationRequest;
import com.foodapp.dto.requests.MessageRequest;
import com.foodapp.dto.response.ApiResponse;
import com.foodapp.dto.response.MessageResponse;
import com.foodapp.services.IMessageService;
import com.foodapp.services.Impl.MessageService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MessageController {

    IMessageService messageService;

    @MessageMapping("/send")
    public void sendMessage(@Payload MessageRequest messageRequest) {
        messageService.handleSaveMessage(messageRequest);
    }

    @PostMapping("/history")
    public ResponseEntity<?> getChatHistory(@Valid @RequestBody ConversationRequest conversationRequest) {
        List<MessageResponse> messages = messageService.getMessagesByConversation(conversationRequest);
        return ResponseEntity.ok(
                ApiResponse.<List<MessageResponse>>builder()
                        .status(ApiResponse.Status.SUCCESS)
                        .data(messages)
                        .build()
        );
    }

    @PostMapping("/mark-read")
    public ResponseEntity<?> markConversationRead(@RequestParam String conversationId,
                                                  @RequestParam String userId) {
        messageService.markAllRead(conversationId, userId);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .status(ApiResponse.Status.SUCCESS)
                        .message("Conversation marked as read.")
                        .build()
        );
    }

}
