package com.example.foodapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodapp.R;
import com.example.foodapp.dto.response.MessageResponse;
import com.example.foodapp.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<MessageResponse> messages;
    private final String currentUserId;

    public ChatAdapter(String currentUserId) {
        this.currentUserId = currentUserId;
        this.messages = new ArrayList<>();
    }

    public void setData(List<MessageResponse> newList) {
        messages.clear();
        messages.addAll(newList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        MessageResponse msg = messages.get(position);
        if (msg.getSenderId().equals(currentUserId)) {
            return 1;
        } else {
            return 2;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 1) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_send, parent, false);
            return new SendViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_received, parent, false);
            return new ReceiveViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MessageResponse msg = messages.get(position);
        if (holder instanceof SendViewHolder) {
            ((SendViewHolder) holder).bind(msg);
        } else if (holder instanceof ReceiveViewHolder) {
            ((ReceiveViewHolder) holder).bind(msg);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    static class SendViewHolder extends RecyclerView.ViewHolder {
        TextView tvContent;
        TextView tvTime;

        public SendViewHolder(@NonNull View itemView) {
            super(itemView);
            tvContent = itemView.findViewById(R.id.chatMessageSend);
            tvTime = itemView.findViewById(R.id.chatMessageSendTime);
        }

        public void bind(MessageResponse msg) {
            tvContent.setText(msg.getContent());
            tvTime.setText(Utils.formatTime(msg.getTimestamp()));

        }
    }

    static class ReceiveViewHolder extends RecyclerView.ViewHolder {
        TextView tvContent;
        TextView tvTime;
        public ReceiveViewHolder(@NonNull View itemView) {
            super(itemView);
            tvContent = itemView.findViewById(R.id.chatMessageReceived);
            tvTime = itemView.findViewById(R.id.chatMessageReceivedTime);
        }

        public void bind(MessageResponse msg) {
            tvContent.setText(msg.getContent());
            tvTime.setText(Utils.formatTime(msg.getTimestamp()));
        }

    }

}
