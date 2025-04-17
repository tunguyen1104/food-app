package com.example.foodapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import java.io.IOException;
import java.security.GeneralSecurityException;

public class GlideUtils {
    public static GlideUrl getAuthorizedGlideUrl(Context context, String url) {
        String accessToken = getAccessToken(context);
        if (accessToken == null || accessToken.isEmpty()) {
            // Return GlideUrl without Authorization header if no token
            return new GlideUrl(url);
        }

        return new GlideUrl(url, new LazyHeaders.Builder()
                .addHeader("Authorization", "Bearer " + accessToken)
                .build());
    }

    private static String getAccessToken(Context context) {
        try {
            SharedPreferences prefs = EncryptedSharedPreferences.create(
                    "auth",
                    MasterKey.DEFAULT_MASTER_KEY_ALIAS,
                    context.getApplicationContext(),
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
            return prefs.getString("access_token", null);
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
            // Fallback to regular SharedPreferences
            return context.getSharedPreferences("auth", Context.MODE_PRIVATE)
                    .getString("access_token", null);
        }
    }
}
