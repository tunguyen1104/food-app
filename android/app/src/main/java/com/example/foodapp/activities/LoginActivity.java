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
                        Toast.makeText(LoginActivity.this,
                                "Login successful",
                                Toast.LENGTH_SHORT).show();
                        // Navigate to next screen
                        navigateToMain();
                    }

                    @Override
                    public void onError(String errorMessage) {
                        Toast.makeText(LoginActivity.this,
                                errorMessage,
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void navigateToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
