package com.example.foodapp.viewmodel.order;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;


public class OrderDetailViewModelFactory implements ViewModelProvider.Factory {

    private final Context context;

    public OrderDetailViewModelFactory(Context context) {
        this.context = context.getApplicationContext();
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(OrderDetailViewModel.class)) {
            //noinspection unchecked
            return (T) new OrderDetailViewModel(context);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}