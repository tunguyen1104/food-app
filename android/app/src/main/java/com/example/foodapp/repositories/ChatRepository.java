package com.example.foodapp.repositories;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.foodapp.dto.request.ConversationRequest;
import com.example.foodapp.dto.request.MessageRequest;
import com.example.foodapp.dto.response.ApiResponse;
import com.example.foodapp.dto.response.ConversationResponse;
import com.example.foodapp.dto.response.MessageResponse;
import com.example.foodapp.network.ApiClient;
import com.example.foodapp.services.ChatService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatRepository {
    private final Context context;
    private final ChatService chatService;

    public ChatRepository(Context context) {
        this.context = context;
        this.chatService = ApiClient.getClient(context).create(ChatService.class);
    }

    public void getAllConversationForAdmin(String adminId, final GetAllConversationCallback callback) {
        Call<ApiResponse<List<ConversationResponse>>> call = chatService.getAllConversation(adminId);
        call.enqueue(new Callback<ApiResponse<List<ConversationResponse>>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<List<ConversationResponse>>> call,
                                   @NonNull Response<ApiResponse<List<ConversationResponse>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    callback.onSuccess(response.body().getData());
                } else {
                    callback.onError("Error retrieving conversation");
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<List<ConversationResponse>>> call, @NonNull Throwable t) {
                callback.onError("Connect error: " + t.getMessage());
            }
        });
    }

    public void createOrFindConversation(ConversationRequest conversationRequest, final CreateOrFindConversationCallback callback) {
        Call<ApiResponse<ConversationResponse>> call = chatService.createOrFindConversation(conversationRequest);
        call.enqueue(new Callback<ApiResponse<ConversationResponse>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<ConversationResponse>> call,
                                   @NonNull Response<ApiResponse<ConversationResponse>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    callback.onSuccess(response.body().getData());
                } else {
                    callback.onError("Error retrieving conversation");
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<ConversationResponse>> call,
                                  @NonNull Throwable t) {
                callback.onError("Connect error: " + t.getMessage());
            }
        });
    }

    public void getChatHistory(ConversationRequest conversationRequest, final ChatHistoryCallback callback) {
        Call<ApiResponse<List<MessageResponse>>> call = chatService.getChatHistory(conversationRequest);
        call.enqueue(new Callback<ApiResponse<List<MessageResponse>>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<List<MessageResponse>>> call,
                                   @NonNull Response<ApiResponse<List<MessageResponse>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<MessageResponse> history = response.body().getData();
                    List<MessageResponse> messages = (history != null)
                            ? history : new ArrayList<>();
                    callback.onSuccess(messages);
                } else {
                    callback.onError("Error loading chat history");
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<List<MessageResponse>>> call, @NonNull Throwable t) {
                callback.onError("Connection error: " + t.getMessage());
            }
        });
    }

    public void markConversationRead(String conversationId, String userId, final MarkReadCallback callback) {
        Call<Void> call = chatService.markConversationRead(conversationId, userId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess();
                } else {
                    callback.onError("Error marking conversation as read");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                callback.onError("Connect error: " + t.getMessage());
            }
        });
    }

    public interface GetAllConversationCallback {
        void onSuccess(List<ConversationResponse> conversations);
        void onError(String message);
    }

    public interface CreateOrFindConversationCallback {
        void onSuccess(ConversationResponse conversation);
        void onError(String message);
    }

    public interface ChatHistoryCallback {
        void onSuccess(List<MessageResponse> messages);
        void onError(String message);
    }

    public interface MarkReadCallback {
        void onSuccess();
        void onError(String message);
    }

}
