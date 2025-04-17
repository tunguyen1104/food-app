package com.foodapp.constants;

public class SecurityConstants {
    public static final String[] BYPASS_ENDPOINTS = {
            "/api/healthcheck/health",
            "/api/auth/signIn",
            "/api/auth/signUp",
            "/api/auth/refresh",
            "/api/food/all",
            "/api/statistics/*"
    };
}
