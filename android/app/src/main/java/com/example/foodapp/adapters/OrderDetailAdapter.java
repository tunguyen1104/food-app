package com.example.foodapp.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodapp.R;
import com.example.foodapp.consts.Constants;
import com.example.foodapp.databinding.ItemOrderDetailBinding;
import com.example.foodapp.dto.response.OrderDetailResponse;
import com.example.foodapp.utils.GlideUtils;

import java.util.List;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.OrderDetailViewHolder> {
    private List<OrderDetailResponse> orderDetails;

    public OrderDetailAdapter(List<OrderDetailResponse> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public void setOrderDetails(List<OrderDetailResponse> orderDetails) {
        this.orderDetails = orderDetails;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OrderDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemOrderDetailBinding binding = ItemOrderDetailBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new OrderDetailViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailViewHolder holder, int position) {
        OrderDetailResponse detail = orderDetails.get(position);
        holder.bind(detail);
    }

    @Override
    public int getItemCount() {
        return orderDetails != null ? orderDetails.size() : 0;
    }

    public static class OrderDetailViewHolder extends RecyclerView.ViewHolder {
        private final ItemOrderDetailBinding binding;

        public OrderDetailViewHolder(ItemOrderDetailBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(OrderDetailResponse detail) {
            binding.name.setText(detail.getFoodName());
            binding.quantity.setText(String.valueOf(detail.getQuantity()));
            binding.price.setText(detail.getUnitPrice() + "$");
            binding.category.setText(detail.getCategoryName());

            if (detail.getAvatarUrl() != null && !detail.getAvatarUrl().isEmpty()) {
                Glide.with(binding.imageProfile.getContext())
                        .load(GlideUtils.getAuthorizedGlideUrl(itemView.getContext(), Constants.URL_HOST_SERVER + detail.getAvatarUrl()))
                        .placeholder(R.drawable.avatar_default)
                        .into(binding.imageProfile);
            } else {
                binding.imageProfile.setImageResource(R.drawable.avatar_default);
            }
        }
    }
}