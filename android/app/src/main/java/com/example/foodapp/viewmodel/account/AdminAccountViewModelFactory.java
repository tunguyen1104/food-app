package com.example.foodapp.viewmodel.account;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class AdminAccountViewModelFactory implements ViewModelProvider.Factory {
    private final Context context;

    public AdminAccountViewModelFactory(Context context) {
        this.context = context.getApplicationContext();
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(AdminAccountViewModel.class)) {
            //noinspection unchecked
            return (T) new AdminAccountViewModel(context);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
