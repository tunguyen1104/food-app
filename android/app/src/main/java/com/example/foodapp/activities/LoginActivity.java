package com.example.foodapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.foodapp.databinding.ActivityLoginBinding;
import com.example.foodapp.dto.state.LoginState;
import com.example.foodapp.viewmodel.BaseViewModelFactory;
import com.example.foodapp.viewmodel.login.LoginViewModel;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private LoginViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this, new BaseViewModelFactory<>(this, LoginViewModel.class))
                .get(LoginViewModel.class);

        setupListeners();
        observeViewModel();
    }

    private void setupListeners() {
        binding.btnLogin.setOnClickListener(v -> {
            String phoneNumber = binding.editTextPhoneNumber.getText().toString().trim();
            String password = binding.editTextPassword.getText().toString().trim();
            viewModel.login(phoneNumber, password);
        });
    }

    private void observeViewModel() {
        // Observe login state
        viewModel.getLoginState().observe(this, state -> {
            if (state == null) return;

            // Reset errors
            binding.editTextPhoneNumber.setError(null);
            binding.editTextPassword.setError(null);

            if (state.isLoading()) {
                binding.btnLogin.setEnabled(false);
                // Show loading indicator if needed
            } else {
                binding.btnLogin.setEnabled(true);
            }

            if (state.getError() != null) {
                handleError(state.getError());
            }

            if (state.isSuccess()) {
                Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
                navigateToMain();
            }
        });

        // Observe input validation errors
        viewModel.getPhoneNumberError().observe(this, error -> {
            if (error != null) {
                binding.editTextPhoneNumber.setError(error);
            }
        });

        viewModel.getPasswordError().observe(this, error -> {
            if (error != null) {
                binding.editTextPassword.setError(error);
            }
        });
    }

    private void handleError(LoginState.Error error) {
        switch (error) {
            case INVALID_INPUT:
                // Errors are handled by phoneNumberError and passwordError LiveData
                break;
            case AUTH_FAILED:
                Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                break;
            case PROFILE_FAILED:
                Toast.makeText(this, "Failed to load profile", Toast.LENGTH_SHORT).show();
                break;
            case NETWORK_ERROR:
                Toast.makeText(this, "Network error, please try again", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void navigateToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish(); // Close LoginActivity
    }
}
