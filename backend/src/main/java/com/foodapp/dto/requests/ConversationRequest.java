package com.foodapp.dto.requests;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConversationRequest {
    private List<String> participantIds;
}
