package com.example.foodapp.viewmodel.message;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.foodapp.dto.response.ConversationResponse;
import com.example.foodapp.repositories.ChatRepository;

import java.util.ArrayList;
import java.util.List;

public class ContactListViewModel extends ViewModel {

    private final MutableLiveData<List<ConversationResponse>> contactsLiveData = new MutableLiveData<>();
    private final ChatRepository chatRepository;

    public LiveData<List<ConversationResponse>> getContacts() {
        return contactsLiveData;
    }

    public ContactListViewModel(Context context) {
        chatRepository = new ChatRepository(context.getApplicationContext());
        contactsLiveData.setValue(new ArrayList<>());
    }

    public LiveData<List<ConversationResponse>> getConversationsLiveData() {
        return contactsLiveData;
    }

    public void loadAllConversationsForAdmin(String adminId) {
        chatRepository.getAllConversationForAdmin(adminId, new ChatRepository.GetAllConversationCallback() {
            @Override
            public void onSuccess(List<ConversationResponse> conversations) {
                contactsLiveData.postValue(conversations);
            }

            @Override
            public void onError(String message) {
                Log.e("ConversationViewModel", "Error loading conversations: " + message);
            }
        });
    }
}
