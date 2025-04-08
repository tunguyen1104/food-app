package com.example.foodapp.viewmodel.account;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class AccountListViewModelFactory implements ViewModelProvider.Factory {
    private final Context context;

    public AccountListViewModelFactory(Context context) {
        this.context = context.getApplicationContext();
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(AccountListViewModel.class)) {
            return (T) new AccountListViewModel(context);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
