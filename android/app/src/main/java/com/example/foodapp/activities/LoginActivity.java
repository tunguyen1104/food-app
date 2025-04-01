package com.example.foodapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.foodapp.R;
import com.example.foodapp.dto.response.AuthResponse;
import com.example.foodapp.repositories.AuthRepository;
import com.google.android.material.button.MaterialButton;

public class LoginActivity extends AppCompatActivity {
    private AuthRepository authRepository;

    private EditText phoneNumberEditText;
    private EditText passwordEditText;
    private MaterialButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        phoneNumberEditText = findViewById(R.id.editTextPhoneNumber);
        passwordEditText = findViewById(R.id.editTextPassword);
        loginButton = findViewById(R.id.btnLogin);

        authRepository = new AuthRepository(this);

        loginButton.setOnClickListener(view -> {
            String phoneNumber = phoneNumberEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            if (validateInput(phoneNumber, password)) {
                performLogin(phoneNumber, password);
            }
        });
    }

    private boolean validateInput(String phoneNumber, String password) {
        if (phoneNumber.isEmpty()) {
            phoneNumberEditText.setError("Phone number is required");
            return false;
        }
        if (password.isEmpty()) {
            passwordEditText.setError("Password is required");
            return false;
        }
        return true;
    }

    private void performLogin(String phoneNumber, String password) {
        authRepository.loginUser(phoneNumber, password,
                new AuthRepository.LoginCallback() {
                    @Override
                    public void onSuccess(AuthResponse authResponse) {
                        // Store tokens securely (e.g., in SharedPreferences)
                        System.out.println(authResponse);
                        saveTokens(authResponse.getAccessToken(),
                                authResponse.getRefreshToken());
                        Toast.makeText(LoginActivity.this,
                                "Login successful",
                                Toast.LENGTH_SHORT).show();
                        // Navigate to next screen
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    }

                    @Override
                    public void onError(String errorMessage) {
                        Toast.makeText(LoginActivity.this,
                                errorMessage,
                                Toast.LENGTH_SHORT).show();
                        System.out.println(errorMessage);
                    }
                });
    }

    private void saveTokens(String accessToken, String refreshToken) {
        // Implement secure token storage
        // Recommended: Use EncryptedSharedPreferences
        getSharedPreferences("auth", MODE_PRIVATE)
                .edit()
                .putString("access_token", accessToken)
                .putString("refresh_token", refreshToken)
                .apply();
    }
}
