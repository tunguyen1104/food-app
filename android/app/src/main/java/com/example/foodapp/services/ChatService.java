package com.example.foodapp.services;

import com.example.foodapp.dto.request.ConversationRequest;
import com.example.foodapp.dto.response.ApiResponse;
import com.example.foodapp.dto.response.ConversationResponse;
import com.example.foodapp.dto.response.MessageResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ChatService {

    @POST("/api/messages/history")
    Call<ApiResponse<List<MessageResponse>>> getChatHistory(@Body ConversationRequest request);

    @POST("/api/messages/mark-read")
    Call<Void> markConversationRead(@Query("conversationId") String conversationId,
                                    @Query("userId") String userId);

    @POST("/api/conversations/create")
    Call<ApiResponse<ConversationResponse>> createOrFindConversation(@Body ConversationRequest request);

    @GET("/api/conversations/admin/{adminId}")
    Call<ApiResponse<List<ConversationResponse>>> getAllConversation(@Path("adminId") String adminId);
}