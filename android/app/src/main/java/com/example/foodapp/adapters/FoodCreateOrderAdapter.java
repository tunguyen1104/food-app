package com.example.foodapp.adapters;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodapp.R;
import com.example.foodapp.consts.Constants;
import com.example.foodapp.databinding.ItemCreateOrderBinding;
import com.example.foodapp.dto.response.FoodDto;
import com.example.foodapp.listeners.CreateOrderAdapterListener;
import com.example.foodapp.utils.GlideUtils;

import java.util.List;

public class FoodCreateOrderAdapter extends RecyclerView.Adapter<FoodCreateOrderAdapter.CreateOrderViewHolder> {
    private List<FoodDto> foodItems;
    private CreateOrderAdapterListener listener;

    public FoodCreateOrderAdapter(List<FoodDto> foodItems, CreateOrderAdapterListener listener) {
        this.foodItems = foodItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FoodCreateOrderAdapter.CreateOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCreateOrderBinding binding = ItemCreateOrderBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new FoodCreateOrderAdapter.CreateOrderViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodCreateOrderAdapter.CreateOrderViewHolder holder, int position) {
        FoodDto foodItem = foodItems.get(position);
        holder.bind(foodItem);
    }

    @Override
    public int getItemCount() {
        return foodItems.size();
    }

    public void updateData(List<FoodDto> newFoodItems) {
        this.foodItems = newFoodItems;
        notifyDataSetChanged();
    }

    public class CreateOrderViewHolder extends RecyclerView.ViewHolder {
        private final ItemCreateOrderBinding binding;

        public CreateOrderViewHolder(ItemCreateOrderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(FoodDto foodItem) {
            binding.name.setText(foodItem.getFoodName());
            binding.desc.setText(String.valueOf(foodItem.getDescription()));
            binding.price.setText(foodItem.getPrice() + "");

            if (foodItem.getAvatarUrl() != null && !foodItem.getAvatarUrl().isEmpty()) {
                Glide.with(binding.image.getContext())
                        .load(GlideUtils.getAuthorizedGlideUrl(itemView.getContext(), Constants.URL_HOST_SERVER + foodItem.getAvatarUrl()))
                        .placeholder(R.drawable.avatar_default)
                        .into(binding.image);
            } else {
                binding.image.setImageResource(R.drawable.banh_mi_thit);
            }
            binding.checkboxSelect.setOnCheckedChangeListener((buttonView, isChecked) -> {
                foodItem.setChecked(isChecked);
                if (listener != null) {
                    listener.onItemCheckedChange(foodItem, isChecked);
                }
            });
            // Quantity change listener
            binding.etQuantity.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    int quantity;
                    try {
                        quantity = Integer.parseInt(s.toString());
                    } catch (NumberFormatException e) {
                        quantity = 1;
                    }
                    foodItem.setQuantity(Math.max(1, quantity)); // Ensure minimum quantity is 1
                    if (listener != null) {
                        listener.onQuantityChange(foodItem, foodItem.getQuantity());
                    }
                }
            });
        }
    }
}
