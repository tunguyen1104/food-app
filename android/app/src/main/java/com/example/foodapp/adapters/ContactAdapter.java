package com.example.foodapp.adapters;


import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodapp.R;
import com.example.foodapp.consts.Constants;
import com.example.foodapp.databinding.ItemChatUserBinding;
import com.example.foodapp.dto.response.ConversationResponse;
import com.example.foodapp.listeners.OnContactClickListener;
import com.example.foodapp.utils.GlideUtils;
import com.example.foodapp.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    private List<ConversationResponse> contacts = new ArrayList<>();
    private OnContactClickListener contactClickListener;

    public ContactAdapter(OnContactClickListener listener) {
        this.contactClickListener = listener;
    }

    public void setContacts(List<ConversationResponse> contacts) {
        this.contacts = contacts;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemChatUserBinding binding = ItemChatUserBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ContactViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        ConversationResponse conversation = contacts.get(position);
        holder.bind(conversation);
        holder.itemView.setOnClickListener(v -> {
            if (contactClickListener != null) {
                contactClickListener.onContactClick(conversation);
            }
        });
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    static class ContactViewHolder extends RecyclerView.ViewHolder {
        private final ItemChatUserBinding binding;

        public ContactViewHolder(ItemChatUserBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(ConversationResponse conversation) {
            binding.textName.setText(conversation.getReceiverName());

            String description = "Last update: " + Utils.formatTime(conversation.getLastUpdate());
            binding.textDescription.setText(description);

            if (conversation.getReceiverAvatarUrl() != null && !conversation.getReceiverAvatarUrl().isEmpty()) {
                Glide.with(binding.imageProfile.getContext())
                        .load(GlideUtils.getAuthorizedGlideUrl(itemView.getContext(), Constants.URL_HOST_SERVER + conversation.getReceiverAvatarUrl()))
                        .placeholder(R.drawable.avatar_default)
                        .into(binding.imageProfile);
            } else {
                binding.imageProfile.setImageResource(R.drawable.avatar_default);
            }
        }
    }
}
