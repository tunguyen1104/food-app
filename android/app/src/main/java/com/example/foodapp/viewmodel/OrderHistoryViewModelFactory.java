package com.example.foodapp.viewmodel;

import android.content.Context;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class OrderHistoryViewModelFactory implements ViewModelProvider.Factory {

    private final Context context;

    public OrderHistoryViewModelFactory(Context context) {
        this.context = context;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(OrderHistoryViewModel.class)) {
            return (T) new OrderHistoryViewModel(context);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
