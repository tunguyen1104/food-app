package com.example.foodapp.fragments.food;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.foodapp.R;

public class FoodContainer extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.container_food, container, false);
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.add(R.id.foodContainer, new EditFoodFragment()).commit();
        return view;
    }
}
