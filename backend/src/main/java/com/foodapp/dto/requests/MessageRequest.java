package com.foodapp.dto.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageRequest implements Serializable {

    @NotBlank(message = "Conversation ID is required")
    private String conversationId;

    @NotBlank(message = "Sender ID is required")
    private String senderId;

    @NotBlank(message = "Receiver ID is required")
    private String receiverId;

    @NotBlank(message = "Content cannot be empty")
    private String content;
}
