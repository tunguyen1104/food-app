package com.example.foodapp.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodapp.R;
import com.example.foodapp.consts.Constants;
import com.example.foodapp.databinding.ItemAccountBranchBinding;
import com.example.foodapp.dto.response.UserResponse;
import com.example.foodapp.listeners.OnAccountClickListener;
import com.example.foodapp.utils.GlideUtils;

import java.util.List;

public class AdminAccountAdapter extends RecyclerView.Adapter<AdminAccountAdapter.AccountViewHolder> {

    private final List<UserResponse> userList;
    private final Context context;
    private final OnAccountClickListener listener;

    public AdminAccountAdapter(Context context, List<UserResponse> userList, OnAccountClickListener listener) {
        this.context = context;
        this.userList = userList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AccountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemAccountBranchBinding binding = ItemAccountBranchBinding.inflate(inflater, parent, false);
        return new AccountViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AccountViewHolder holder, int position) {
        UserResponse user = userList.get(position);
        holder.bind(user);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    class AccountViewHolder extends RecyclerView.ViewHolder {
        private final ItemAccountBranchBinding binding;

        public AccountViewHolder(ItemAccountBranchBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(UserResponse user) {
            binding.branchName.setText(user.getFullName());
            binding.email.setText(user.getUserName());
            binding.phone.setText(user.getPhone());
            // Glide load avatar
            if (user.getAvatarUrl() != null && !user.getAvatarUrl().isEmpty()) {
                Glide.with(context)
                        .load(GlideUtils.getAuthorizedGlideUrl(itemView.getContext(), Constants.URL_HOST_SERVER + user.getAvatarUrl()))
                        .placeholder(R.drawable.avatar_default)
                        .into(binding.imageProfile);
            } else {
                binding.imageProfile.setImageResource(R.drawable.avatar_default);
            }

            if (user.getPhone() != null && !user.getPhone().isEmpty()) {
                Glide.with(context)
                        .load(R.drawable.ic_google)
                        .placeholder(R.drawable.ic_google)
                        .into(binding.googleOrPhoneIcon);
            } else {
                binding.googleOrPhoneIcon.setImageResource(R.drawable.ic_google);
            }

            binding.location.setText(user.getLocation());

            binding.getRoot().setOnClickListener(v -> listener.onAccountClick(user));
        }
    }
}
