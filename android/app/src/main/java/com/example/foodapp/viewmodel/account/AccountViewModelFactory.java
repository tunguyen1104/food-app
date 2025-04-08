package com.example.foodapp.viewmodel.account;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class AccountViewModelFactory implements ViewModelProvider.Factory {
    private final Context context;

    public AccountViewModelFactory(Context context) {
        this.context = context.getApplicationContext();
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(AccountViewModel.class)) {
            //noinspection unchecked
            return (T) new AccountViewModel(context);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
