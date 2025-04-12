package com.example.foodapp.dto.request;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ConversationRequest implements Serializable {
    private List<String> participantIds;

    public ConversationRequest(List<String> participantIds) {
        this.participantIds = participantIds;
    }
}
