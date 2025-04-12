package com.foodapp.controllers;

import com.foodapp.dto.requests.ConversationRequest;
import com.foodapp.dto.response.ApiResponse;
import com.foodapp.dto.response.ConversationResponse;
import com.foodapp.services.IConversationService;
import com.foodapp.services.Impl.ConversationService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/conversations")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ConversationController {

    IConversationService conversationService;

    @PostMapping("/create")
    public ResponseEntity<?> createOrFindConversation(@Valid @RequestBody ConversationRequest request) {
        ConversationResponse conv = conversationService.createOrFindConversation(request.getParticipantIds());
        return ResponseEntity.ok(
                ApiResponse.<ConversationResponse>builder()
                        .status(ApiResponse.Status.SUCCESS)
                        .data(conv)
                        .build()
        );
    }

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/admin/{adminId}")
    public ResponseEntity<?> getAllConversation(@PathVariable String adminId) {
        List<ConversationResponse> conversations = conversationService.getAllConversationForAdmin(adminId);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .status(ApiResponse.Status.SUCCESS)
                        .data(conversations)
                        .build()
        );
    }
}
