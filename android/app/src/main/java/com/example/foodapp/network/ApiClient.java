package com.example.foodapp.network;

import android.content.Context;

import com.example.foodapp.utils.AuthInterceptor;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String baseUrl = "http://10.0.2.2:8081";
    private static Retrofit retrofit;
    private static AuthInterceptor authInterceptor;

    public static Retrofit getClient(Context context) {
        if (retrofit == null) {
            authInterceptor = new AuthInterceptor(context);

            OkHttpClient client = new OkHttpClient()
                    .newBuilder()
                    .addInterceptor(authInterceptor)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofit;
    }

    public static void updateAccessToken(String newAccessToken) {
        if (authInterceptor != null) {
            authInterceptor.updateAccessToken(newAccessToken);
        }
    }
}
