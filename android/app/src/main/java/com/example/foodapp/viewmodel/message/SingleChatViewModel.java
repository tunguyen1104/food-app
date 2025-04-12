package com.example.foodapp.viewmodel.message;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.foodapp.dto.request.ConversationRequest;
import com.example.foodapp.dto.response.ConversationResponse;
import com.example.foodapp.dto.response.MessageResponse;
import com.example.foodapp.repositories.ChatRepository;

import java.util.ArrayList;
import java.util.List;

public class SingleChatViewModel extends ViewModel {

    private final MutableLiveData<List<MessageResponse>> messageList = new MutableLiveData<>();
    private final ChatRepository chatRepository;
    private final MutableLiveData<ConversationResponse> conversationLiveData = new MutableLiveData<>();
    public SingleChatViewModel(Context context) {
        chatRepository = new ChatRepository(context.getApplicationContext());
        messageList.setValue(new ArrayList<>());
    }

    public LiveData<List<MessageResponse>> getMessageList() {
        return messageList;
    }

    public LiveData<ConversationResponse> getConversation() {
        return conversationLiveData;
    }

    public void loadHistory(ConversationRequest conversationRequest) {
        chatRepository.getChatHistory(conversationRequest, new ChatRepository.ChatHistoryCallback() {
            @Override
            public void onSuccess(List<MessageResponse> messages) {
                Log.d("SingleChatViewModel", "Loaded history: " + (messages != null ? messages.size() : "null"));
                messageList.postValue(messages);
            }

            @Override
            public void onError(String errorMessage) {
                Log.e("SingleChatViewModel", "Error loading history: " + errorMessage);
            }
        });
    }

    public void addMessage(MessageResponse msg) {
        List<MessageResponse> currentMessages = messageList.getValue();
        if (currentMessages == null) {
            currentMessages = new ArrayList<>();
        }
        currentMessages.add(msg);
        messageList.postValue(currentMessages);
    }

    public void markConversationRead(String conversationId, String userId) {
        chatRepository.markConversationRead(conversationId, userId, new ChatRepository.MarkReadCallback() {
            @Override
            public void onSuccess() {
                // Bạn có thể thực hiện cập nhật LiveData hoặc thông báo thành công
            }

            @Override
            public void onError(String errorMessage) {
                // Xử lý lỗi nếu cần
            }
        });
    }

    public void createOrFindConversation(ConversationRequest conversationRequest) {
        chatRepository.createOrFindConversation(conversationRequest, new ChatRepository.CreateOrFindConversationCallback() {
            @Override
            public void onSuccess(ConversationResponse conversationResponse) {
                conversationLiveData.postValue(conversationResponse);
            }

            @Override
            public void onError(String errorMessage) {
                Log.e("SingleChatViewModel", "Error getting conversation: " + errorMessage);
            }
        });
    }
}
