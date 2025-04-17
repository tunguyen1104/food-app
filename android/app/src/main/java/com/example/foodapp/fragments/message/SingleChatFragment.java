package com.example.foodapp.fragments.message;

import static com.example.foodapp.consts.Constants.ID_ADMIN_DEFAULT;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.foodapp.R;
import com.example.foodapp.adapters.ChatAdapter;
import com.example.foodapp.consts.Constants;
import com.example.foodapp.databinding.FragmentSingleChatBinding;
import com.example.foodapp.dto.request.ConversationRequest;
import com.example.foodapp.dto.response.ConversationResponse;
import com.example.foodapp.dto.response.MessageResponse;
import com.example.foodapp.dto.response.NotificationResponse;
import com.example.foodapp.dto.response.UserResponse;
import com.example.foodapp.enums.MessageStatus;
import com.example.foodapp.enums.NotificationType;
import com.example.foodapp.network.StompManager;
import com.example.foodapp.utils.GlideUtils;
import com.example.foodapp.utils.NavigationUtil;
import com.example.foodapp.utils.NotificationUtil;
import com.example.foodapp.utils.UserManager;
import com.example.foodapp.utils.Utils;
import com.example.foodapp.viewmodel.BaseViewModelFactory;
import com.example.foodapp.viewmodel.message.SingleChatViewModel;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Date;

public class SingleChatFragment extends Fragment {

    private FragmentSingleChatBinding binding;
    private SingleChatViewModel chatViewModel;
    private ChatAdapter chatAdapter;
    private StompManager stompManager;

    private String currentUserId = ID_ADMIN_DEFAULT;
    private String currentReceiverId = ID_ADMIN_DEFAULT;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSingleChatBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        NavigationUtil.setupBackNavigation(this, binding.buttonBack);
        NotificationUtil.createNotificationChannel(requireContext());
        chatViewModel = new ViewModelProvider(this,
                new BaseViewModelFactory<>(requireContext(), SingleChatViewModel.class))
                .get(SingleChatViewModel.class);


        UserResponse currentUser = UserManager.getUser(requireContext());
        currentUserId = (currentUser != null) ? String.valueOf(currentUser.getId()) : ID_ADMIN_DEFAULT;

        // Lấy ConversationResponse từ arguments nếu có, hoặc từ API nếu admin
        ConversationResponse conversationResponse = null;
        if (getArguments() != null && getArguments().containsKey("conversation")) {
            conversationResponse = (ConversationResponse) getArguments().getSerializable("conversation");
        } else if (!currentUserId.equals(ID_ADMIN_DEFAULT)) {

            binding.buttonBack.setVisibility(View.GONE);

            ConversationRequest request = new ConversationRequest(Arrays.asList(currentReceiverId, currentUserId));
            chatViewModel.createOrFindConversation(request);
            conversationResponse = chatViewModel.getConversation().getValue();
        }


        if (conversationResponse != null && conversationResponse.getParticipantIds() != null
                && conversationResponse.getParticipantIds().size() >= 2) {
            currentReceiverId = conversationResponse.getParticipantIds().get(1);
            updateConversationUI(conversationResponse);
        }


        chatAdapter = new ChatAdapter(currentUserId);
        binding.chatRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.chatRecyclerView.setAdapter(chatAdapter);

        chatViewModel.getConversation().observe(getViewLifecycleOwner(), convResponse -> {
            if (convResponse != null) {
                String description = "Last update: " + Utils.formatTime(convResponse.getLastUpdate());
                binding.textDescription.setText(description);
                binding.textName.setText("Tu Nguyen");
            } else {
                Toast.makeText(getContext(), "Conversation not found.", Toast.LENGTH_SHORT).show();
            }
        });


        chatViewModel.getMessageList().observe(getViewLifecycleOwner(), messages -> {
            chatAdapter.setData(messages);
            if (messages != null && !messages.isEmpty()) {
                binding.chatRecyclerView.smoothScrollToPosition(messages.size() - 1);
            }
        });

        ConversationRequest request = new ConversationRequest(Arrays.asList(currentUserId, currentReceiverId));
        chatViewModel.loadHistory(request);

        stompManager = StompManager.getInstance();
        stompManager.connect();
        stompManager.subscribeTo("/topic/messages/" + currentUserId, payload -> {
            try {
                MessageResponse msg = new Gson().fromJson(payload, MessageResponse.class);
                chatViewModel.addMessage(msg);
            } catch (Exception e) {
                Log.e("SingleChatFragment", "Error parsing STOMP message: " + e.getMessage());
            }
        });

        stompManager.subscribeTo("/topic/notifications/" + currentUserId, payload -> {
            try {
                NotificationResponse notification = new Gson().fromJson(payload, NotificationResponse.class);

                NotificationUtil.showNotification(requireContext(), notification);

            } catch (Exception e) {
                Log.e("STOMP_NOTIFICATION", "Error parsing: " + e.getMessage());
            }
        });

        binding.btnSend.setOnClickListener(v -> {
            String text = binding.newMessageEditText.getText().toString().trim();
            if (text.isEmpty()) {
                Toast.makeText(getContext(), "Please enter a message", Toast.LENGTH_SHORT).show();
                return;
            }

            JSONObject messagePayload = new JSONObject();
            try {
                messagePayload.put("senderId", currentUserId);
                messagePayload.put("receiverId", currentReceiverId);
                messagePayload.put("content", text);
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Lỗi định dạng tin nhắn", Toast.LENGTH_SHORT).show();
                return;
            }


            stompManager.sendMessage("/app/send", messagePayload.toString());


            JSONObject notiPayload = new JSONObject();
            try {
                notiPayload.put("userId", currentReceiverId);
                notiPayload.put("title", "You have new message form " + currentUserId);
                notiPayload.put("message", text);
                notiPayload.put("type", NotificationType.MESSAGE);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            stompManager.sendMessage("/app/notify", notiPayload.toString());


            MessageResponse localMsg = new MessageResponse();
            localMsg.setSenderId(currentUserId);
            localMsg.setReceiverId(currentReceiverId);
            localMsg.setContent(text);
            localMsg.setTimestamp(new Date());
            localMsg.setStatus(MessageStatus.SENT);

            chatViewModel.addMessage(localMsg);
            binding.newMessageEditText.setText("");
            binding.newMessageEditText.requestFocus();
            binding.chatRecyclerView.post(() -> {
                if (chatAdapter.getItemCount() > 0) {
                    binding.chatRecyclerView.smoothScrollToPosition(chatAdapter.getItemCount() - 1);
                }
            });
        });
    }

    private void updateConversationUI(ConversationResponse conversation) {
        // Cập nhật tên và description
        binding.textName.setText(conversation.getReceiverName());
        String description = "Last update: " + Utils.formatTime(conversation.getLastUpdate());
        binding.textDescription.setText(description);

        // Cập nhật avatar
        if (conversation.getReceiverAvatarUrl() != null && !conversation.getReceiverAvatarUrl().isEmpty()) {
            Glide.with(binding.imageProfile.getContext())
                    .load(GlideUtils.getAuthorizedGlideUrl(getContext(), Constants.URL_HOST_SERVER
                            + conversation.getReceiverAvatarUrl()))
                    .placeholder(R.drawable.avatar_default)
                    .into(binding.imageProfile);
        } else {
            binding.imageProfile.setImageResource(R.drawable.avatar_default);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
