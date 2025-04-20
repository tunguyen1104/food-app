package com.example.foodapp.repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.foodapp.services.OcrService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OcrRepository {

    private final OcrService ocrService;

    public OcrRepository() {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.ocr.space/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        ocrService = retrofit.create(OcrService.class);
    }

    public void sendToOCRSpace(String base64Image, MutableLiveData<String> ocrResult) {
        Call<ResponseBody> call = ocrService.parseImage(
                "data:image/png;base64," + base64Image,
                "eng",
                "K85923113388957"
        );

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String raw = response.body().string();
                        Log.d("OCR Raw Response", raw);

                        JSONObject json = new JSONObject(raw);
                        JSONArray results = json.getJSONArray("ParsedResults");

                        if (results.length() == 0) {
                            ocrResult.postValue("No text found");
                            return;
                        }

                        String parsedText = results.getJSONObject(0).getString("ParsedText");

                        StringBuilder filtered = new StringBuilder();
                        for (String line : parsedText.split("\\r?\\n")) {
                            if (line.toLowerCase().contains("lx") || line.matches(".*\\d+x.*")) {
                                filtered.append(line).append("\n");
                            }
                        }

                        ocrResult.postValue(filtered.toString().trim());

                    } catch (Exception e) {
                        Log.e("OCR Exception", e.getMessage());
                        ocrResult.postValue("OCR parsing error");
                    }
                } else {
                    ocrResult.postValue("OCR failed: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                ocrResult.postValue("OCR error: " + t.getMessage());
            }
        });
    }
}
