package com.example.foodapp.viewmodel;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class BaseViewModelFactory<T extends ViewModel> implements ViewModelProvider.Factory {
    private final Context context;
    private final Class<T> viewModelClass;

    public BaseViewModelFactory(Context context, Class<T> viewModelClass) {
        this.context = context.getApplicationContext();
        this.viewModelClass = viewModelClass;
    }

    @NonNull
    @Override
    public <V extends ViewModel> V create(@NonNull Class<V> modelClass) {
        if (viewModelClass.isAssignableFrom(modelClass)) {
            try {
                return (V) viewModelClass.getDeclaredConstructor(Context.class).newInstance(context);
            } catch (Exception e) {
                throw new RuntimeException("Cannot create an instance of " + modelClass.getName(), e);
            }
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}
