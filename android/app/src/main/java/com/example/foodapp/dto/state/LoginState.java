package com.example.foodapp.dto.state;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LoginState {
    private boolean isLoading;
    private boolean isSuccess;
    private Error error;

    public boolean isLoading() {
        return isLoading;
    }

    public enum Error {
        INVALID_INPUT,
        AUTH_FAILED,
        PROFILE_FAILED,
        NETWORK_ERROR
    }
}
