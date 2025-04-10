package com.example.foodapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodapp.R;
import com.example.foodapp.dto.response.FoodDto;

import java.util.List;

public class FoodHomeAdapter extends RecyclerView.Adapter<FoodHomeAdapter.FoodViewHolder> {
    private List<FoodDto> foodItems;

    public FoodHomeAdapter(List<FoodDto> foodItems) {
        this.foodItems = foodItems;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_food_home, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
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

    static class FoodViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView nameTextView;
        private TextView priceTextView;
        private TextView priceVoucherTextView;
        private TextView soldTextView;
        private TextView timeTextView;
        private TextView descTextView;
        private ImageView btnEdit;
        private ImageView btnOrder;
        private ImageView btnDelete;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            nameTextView = itemView.findViewById(R.id.name);
            priceTextView = itemView.findViewById(R.id.price);
            priceVoucherTextView = itemView.findViewById(R.id.priceVoucher);
            soldTextView = itemView.findViewById(R.id.sold);
            timeTextView = itemView.findViewById(R.id.time);
            descTextView = itemView.findViewById(R.id.desc);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnOrder = itemView.findViewById(R.id.btnOrder);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }

        public void bind(FoodDto foodItem) {
            nameTextView.setText(foodItem.getFoodName());
            priceTextView.setText(String.format("%.2f", foodItem.getPrice()));

            descTextView.setText(foodItem.getDescription());

            // Load image using Glide (or another image loading library)
            Glide.with(itemView.getContext())
                    .load(foodItem.getAvatarUrl())
                    .placeholder(R.drawable.banh_mi_thit) // Default placeholder
                    .into(imageView);

            // Example click listeners for buttons
            btnEdit.setOnClickListener(v -> {
                Toast.makeText(itemView.getContext(), "Edit: " + foodItem.getFoodName(), Toast.LENGTH_SHORT).show();
            });
            btnDelete.setOnClickListener(v -> {
                Toast.makeText(itemView.getContext(), "Delete: " + foodItem.getFoodName(), Toast.LENGTH_SHORT).show();
            });
        }
    }
}
