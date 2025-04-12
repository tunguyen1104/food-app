package com.example.foodapp.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodapp.R;
import com.example.foodapp.consts.Constants;
import com.example.foodapp.databinding.ItemFoodHomeBinding;
import com.example.foodapp.dto.response.FoodDto;
import com.example.foodapp.listeners.OnFoodClickListener;
import com.example.foodapp.utils.AuthInterceptor;

import java.util.List;

public class FoodHomeAdapter extends RecyclerView.Adapter<FoodHomeAdapter.FoodViewHolder> {
    private final OnFoodClickListener editListener;
    private final OnFoodClickListener deleteListener;
    private List<FoodDto> foodItems;

    public FoodHomeAdapter(List<FoodDto> foodItems, OnFoodClickListener editListener, OnFoodClickListener deleteListener) {
        this.foodItems = foodItems;
        this.editListener = editListener;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFoodHomeBinding binding = ItemFoodHomeBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new FoodViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        FoodDto foodItem = foodItems.get(position);
        holder.bind(foodItem);
        holder.binding.btnEdit.setOnClickListener(v -> editListener.onClick(foodItem));
        holder.binding.btnDelete.setOnClickListener(v -> deleteListener.onClick(foodItem));
    }

    @Override
    public int getItemCount() {
        return foodItems.size();
    }

    public void updateData(List<FoodDto> newFoodItems) {
        this.foodItems = newFoodItems;
        notifyDataSetChanged();
    }

    public static class FoodViewHolder extends RecyclerView.ViewHolder {
        private final ItemFoodHomeBinding binding;

        public FoodViewHolder(ItemFoodHomeBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(FoodDto foodItem) {
            binding.name.setText(foodItem.getFoodName());
            binding.desc.setText(String.valueOf(foodItem.getDescription()));
            binding.price.setText(foodItem.getPrice() + "");

            if (foodItem.getAvatarUrl() != null && !foodItem.getAvatarUrl().isEmpty()) {
                Glide.with(binding.image.getContext())
                        .load(AuthInterceptor.getAuthorizedGlideUrl(Constants.URL_HOST_SERVER + foodItem.getAvatarUrl()))
                        .placeholder(R.drawable.avatar_default)
                        .into(binding.image);
            } else {
                binding.image.setImageResource(R.drawable.banh_mi_thit);
            }
        }
    }
}
