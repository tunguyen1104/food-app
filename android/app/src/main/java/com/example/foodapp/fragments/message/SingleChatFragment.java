package com.example.foodapp.fragments.message;

import static com.example.foodapp.consts.Constants.ARG_CONVERSATION;

import android.Manifest;
import android.os.Bundle;
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

import java.util.Date;
import java.util.List;
import java.util.Optional;

public class SingleChatFragment extends Fragment {

    private FragmentSingleChatBinding binding;
    private SingleChatViewModel viewModel;
    private ChatAdapter chatAdapter;

    private StompManager stompManager;

    private UserResponse currentUser;
    private String receiverId = Constants.ID_ADMIN_DEFAULT;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSingleChatBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    @Override
    public void onViewCreated(@NonNull View v, Bundle savedState) {
        super.onViewCreated(v, savedState);

        initViewModel();
        initToolbar();
        initRecyclerView();
        initStomp();

        ConversationResponse conv = fetchOrCreateConversation();
        if (conv != null) {
            receiverId = getReceiverId(conv.getParticipantIds());
            updateConversationUI(conv);
            viewModel.loadHistory(new ConversationRequest(List.of(currentUser.getId().toString(), receiverId)));
        }
        observeLiveData();
        initSendButton();
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(
                this,
                new BaseViewModelFactory<>(requireContext(), SingleChatViewModel.class)
        ).get(SingleChatViewModel.class);

        currentUser = Optional.ofNullable(UserManager.getUser(requireContext()))
                .orElseThrow(() -> new IllegalStateException("User is not logged in"));
    }

    private void initToolbar() {
        NavigationUtil.setupBackNavigation(this, binding.buttonBack);
        NotificationUtil.createNotificationChannel(requireContext());
    }

    private void initRecyclerView() {
        chatAdapter = new ChatAdapter(currentUser.getId().toString());
        binding.chatRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.chatRecyclerView.setAdapter(chatAdapter);
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    private void initStomp() {
        stompManager = StompManager.getInstance();
        stompManager.connect();

        stompManager.subscribeTo("/topic/messages/" + currentUser.getId().toString(), payload -> {
            MessageResponse msg = new Gson().fromJson(payload, MessageResponse.class);
            viewModel.addMessage(msg);
        });

        stompManager.subscribeTo("/topic/notifications/" + currentUser.getId().toString(), payload -> {
            NotificationResponse notify = new Gson().fromJson(payload, NotificationResponse.class);
            NotificationUtil.showNotification(requireContext(), notify);
        });
    }

    private ConversationResponse fetchOrCreateConversation() {
        // admin
        if (getArguments() != null && getArguments().containsKey(ARG_CONVERSATION)) {
            return (ConversationResponse) getArguments().getSerializable(ARG_CONVERSATION);
        }

        // employee
        binding.buttonBack.setVisibility(View.GONE);
        ConversationRequest req = new ConversationRequest(List.of(currentUser.getId().toString(), receiverId));
        viewModel.createOrFindConversation(req);
        return viewModel.getConversation().getValue();
    }

    private String getReceiverId(java.util.List<String> participantIds) {
        return participantIds.stream()
                .filter(id -> !id.equals(currentUser.getId().toString()))
                .findFirst()
                .orElse(Constants.ID_ADMIN_DEFAULT);
    }

    private void observeLiveData() {
        viewModel.getConversation().observe(getViewLifecycleOwner(), conv -> {
            if (conv != null) {
                receiverId = getReceiverId(conv.getParticipantIds());
                updateConversationUI(conv);

                viewModel.loadHistory(new ConversationRequest(List.of(currentUser.getId().toString(), receiverId)));
            } else {
                Toast.makeText(getContext(), "Not find conversation", Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.getMessageList().observe(getViewLifecycleOwner(), messages -> {
            chatAdapter.setData(messages);
            if (messages != null && !messages.isEmpty()) {
                binding.chatRecyclerView.smoothScrollToPosition(messages.size() - 1);

                MessageResponse last = messages.get(messages.size() - 1);
                updateLastUpdate(last.getTimestamp());
            }
        });
    }

    private void initSendButton() {
        binding.btnSend.setOnClickListener(v -> {
            String text = binding.newMessageEditText.getText().toString().trim();
            if (text.isEmpty()) {
                Toast.makeText(getContext(), "Please enter a message", Toast.LENGTH_SHORT).show();
                return;
            }
            sendMessage(text);
        });
    }

    private void sendMessage(String text) {
        // Build & send message payload
        JSONObject messageJson = new JSONObject();
        try {
            messageJson.put("senderId",  currentUser.getId().toString());
            messageJson.put("receiverId", receiverId);
            messageJson.put("content",   text);
            stompManager.sendMessage("/app/send", messageJson.toString());
        } catch (JSONException e) {
            Toast.makeText(getContext(), "Invalid message payload", Toast.LENGTH_SHORT).show();
            return;
        }

        // Build & send notification payload
        try {
            JSONObject notifyJson = new JSONObject();
            notifyJson.put("userId",  receiverId);
            notifyJson.put("title",   "You have a new message");
            notifyJson.put("message", text);
            notifyJson.put("type",    NotificationType.MESSAGE);
            stompManager.sendMessage("/app/notify", notifyJson.toString());
        } catch (JSONException ignore) {}

        // Add local copy to RecyclerView
        MessageResponse local = new MessageResponse();
        local.setSenderId(currentUser.getId().toString());
        local.setReceiverId(receiverId);
        local.setContent(text);
        local.setTimestamp(new Date());
        local.setStatus(MessageStatus.SENT);
        viewModel.addMessage(local);

        // Clear input UI
        binding.newMessageEditText.setText("");
        binding.chatRecyclerView.post(() -> {
            if (chatAdapter.getItemCount() > 0) {
                binding.chatRecyclerView.smoothScrollToPosition(chatAdapter.getItemCount() - 1);
            }
        });
    }

    private void updateLastUpdate(Date time) {
        String formatted = Utils.formatTime(time);
        String desc = getString(R.string.last_update_format, formatted);
        binding.textDescription.setText(desc);
    }

    private void updateConversationUI(ConversationResponse conv) {
        if (conv == null) return;

        binding.textName.setText(conv.getReceiverName());
        updateLastUpdate(conv.getLastUpdate());

        String url = conv.getReceiverAvatarUrl();
        if (url != null && !url.isEmpty()) {
            Glide.with(this)
                    .load(GlideUtils.getAuthorizedGlideUrl(getContext(), Constants.URL_HOST_SERVER + url))
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
