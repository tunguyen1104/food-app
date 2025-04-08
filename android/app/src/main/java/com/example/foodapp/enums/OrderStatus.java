package com.example.foodapp.enums;

import com.example.foodapp.R;

import lombok.Getter;

@Getter
public enum OrderStatus {
    PROCESSING("Processing", R.color.tag_other),
    COMPLETED("Completed", R.color.tag_course_material),
    CANCELLED("Cancelled", R.color.tag_assignment);

    private final String label;
    private final int colorResId;

    OrderStatus(String label, int colorResId) {
        this.label = label;
        this.colorResId = colorResId;
    }

}