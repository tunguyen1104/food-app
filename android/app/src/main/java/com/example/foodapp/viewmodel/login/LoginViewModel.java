package com.example.foodapp.viewmodel.login;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import com.example.foodapp.dto.response.AuthResponse;
import com.example.foodapp.dto.response.UserResponse;
import com.example.foodapp.dto.state.LoginState;
import com.example.foodapp.repositories.AuthRepository;
import com.example.foodapp.repositories.UserRepository;
import com.example.foodapp.utils.UserManager;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class LoginViewModel extends ViewModel {
    private final AuthRepository authRepository;
    private final UserRepository userRepository;
    private final Context context;

    private final MutableLiveData<LoginState> loginState = new MutableLiveData<>();
    private final MutableLiveData<String> phoneNumberError = new MutableLiveData<>();
    private final MutableLiveData<String> passwordError = new MutableLiveData<>();

    public LoginViewModel(Context context) {
        this.context = context;
        this.authRepository = new AuthRepository(context);
        this.userRepository = new UserRepository(context);
    }

    public void login(String phoneNumber, String password) {
        phoneNumberError.setValue(null);
        passwordError.setValue(null);

        // Validate input
        if (!validateInput(phoneNumber, password)) {
            loginState.setValue(LoginState.builder().error(LoginState.Error.INVALID_INPUT).build());
            return;
        }

        // Set loading state
        loginState.setValue(LoginState.builder().isLoading(true).build());

        // Perform login
        authRepository.loginUser(phoneNumber, password, new AuthRepository.LoginCallback() {
            @Override
            public void onSuccess(AuthResponse authResponse) {
                saveTokens(authResponse.getAccessToken(), authResponse.getRefreshToken());

                userRepository.getUserProfile(new UserRepository.UserProfileCallback() {
                    @Override
                    public void onSuccess(UserResponse user) {
                        UserManager.saveUser(context, user);
                        loginState.setValue(LoginState.builder().isSuccess(true).build());
                    }

                    @Override
                    public void onError(String errorMessage) {
                        loginState.setValue(LoginState.builder().error(LoginState.Error.PROFILE_FAILED).build());
                    }
                });
            }

            @Override
            public void onError(String errorMessage) {
                loginState.setValue(LoginState.builder().error(
                                errorMessage.contains("network") ?
                                        LoginState.Error.NETWORK_ERROR :
                                        LoginState.Error.AUTH_FAILED)
                        .build()
                );
            }
        });
    }

    private boolean validateInput(String phoneNumber, String password) {
        boolean isValid = true;

        if (phoneNumber.isEmpty()) {
            phoneNumberError.setValue("Phone number is required");
            isValid = false;
        }

        if (password.isEmpty()) {
            passwordError.setValue("Password is required");
            isValid = false;
        }

        return isValid;
    }

    private void saveTokens(String accessToken, String refreshToken) {
        // Use EncryptedSharedPreferences for secure storage
        try {
            SharedPreferences prefs = EncryptedSharedPreferences.create(
                    "auth",
                    MasterKey.DEFAULT_MASTER_KEY_ALIAS,
                    context,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
            prefs.edit()
                    .putString("access_token", accessToken)
                    .putString("refresh_token", refreshToken)
                    .apply();
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
            // Fallback to regular SharedPreferences
            context.getSharedPreferences("auth", Context.MODE_PRIVATE)
                    .edit()
                    .putString("access_token", accessToken)
                    .putString("refresh_token", refreshToken)
                    .apply();
        }
    }

    public LiveData<LoginState> getLoginState() {
        return loginState;
    }

    public LiveData<String> getPhoneNumberError() {
        return phoneNumberError;
    }

    public LiveData<String> getPasswordError() {
        return passwordError;
    }
}
