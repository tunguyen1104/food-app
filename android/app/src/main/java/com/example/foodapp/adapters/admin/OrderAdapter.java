package com.example.foodapp.adapters.admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodapp.R;
import com.example.foodapp.databinding.ItemOrderHistoryBinding;
import com.example.foodapp.dto.response.OrderResponse;
import com.example.foodapp.listeners.OnOrderClickListener;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private final Context context;
    private List<OrderResponse> orders;
    private final OnOrderClickListener listener;

    public OrderAdapter(Context context, List<OrderResponse> orders, OnOrderClickListener listener) {
        this.context = context;
        this.orders = orders;
        this.listener = listener;
    }

    public void updateData(List<OrderResponse> newOrders) {
        this.orders = newOrders;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemOrderHistoryBinding binding = ItemOrderHistoryBinding.inflate(LayoutInflater.from(context), parent, false);
        return new OrderViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        OrderResponse order = orders.get(position);
        holder.bind(order);
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    class OrderViewHolder extends RecyclerView.ViewHolder {

        private final ItemOrderHistoryBinding binding;

        public OrderViewHolder(ItemOrderHistoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(OrderResponse order) {
            binding.totalPrice.setText(String.format("%s $", order.getTotalPrice()));
            binding.quantity.setText(String.valueOf(order.getQuantity()));
            binding.orderPlatform.setText(order.getOrderPlatform().name());
            binding.title.setText(order.getDescription());
            binding.time.setText(order.getOrderDate().toString());
            binding.location.setText("Location: TBD");
            binding.description.setText(order.getDescription());

            switch (order.getStatus()) {
                case PROCESSING:
                    binding.tag.setBackgroundColor(ContextCompat.getColor(binding.getRoot().getContext(), R.color.tag_other));
                    break;
                case COMPLETED:
                    binding.tag.setBackgroundColor(ContextCompat.getColor(binding.getRoot().getContext(), R.color.tag_course_material));
                    break;
                case CANCELLED:
                    binding.tag.setBackgroundColor(ContextCompat.getColor(binding.getRoot().getContext(), R.color.tag_assignment));
                    break;
            }
            binding.getRoot().setOnClickListener(v -> listener.onOrderClick(order));
        }
    }
}