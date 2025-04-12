package com.example.foodapp.dto.response;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FoodDto implements Parcelable {
    public static final Creator<FoodDto> CREATOR = new Creator<FoodDto>() {
        @Override
        public FoodDto createFromParcel(Parcel in) {
            return new FoodDto(in);
        }

        @Override
        public FoodDto[] newArray(int size) {
            return new FoodDto[size];
        }
    };
    private Long id;
    @SerializedName("foodName")
    private String foodName;
    @SerializedName("description")
    private String description;
    @SerializedName("price")
    private Double price;
    @SerializedName("categoryName")
    private String categoryName;
    @SerializedName("categoryId")
    private Long categoryId;
    @SerializedName("avatarUrl")
    private String avatarUrl;
    private volatile boolean isChecked;
    private volatile Integer quantity;
    private volatile Integer time;
    @SerializedName("ingredients")
    private List<IngredientDto> ingredients;

    protected FoodDto(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        foodName = in.readString();
        description = in.readString();
        if (in.readByte() == 0) {
            price = null;
        } else {
            price = in.readDouble();
        }
        categoryName = in.readString();
        avatarUrl = in.readString();
        isChecked = in.readByte() != 0;
        if (in.readByte() == 0) {
            quantity = null;
        } else {
            quantity = in.readInt();
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(id.toString());
        parcel.writeString(foodName);
        parcel.writeString(description);
        parcel.writeString(price.toString());
        parcel.writeString(categoryName);
    }
}
