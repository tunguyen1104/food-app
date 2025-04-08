package com.example.foodapp.network;

import android.content.Context;

import com.example.foodapp.consts.Constants;
import com.example.foodapp.utils.AuthInterceptor;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
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
                    .baseUrl(Constants.URL_HOST_SERVER)
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
