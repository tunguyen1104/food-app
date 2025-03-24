package com.foodapp.constants;

public class SecurityConstants {
    public static final String[] BYPASS_ENDPOINTS = {
            "/healthcheck/health",
            "/auth/signIn",
            "/auth/signUp",
            "/auth/refresh"
    };
}
