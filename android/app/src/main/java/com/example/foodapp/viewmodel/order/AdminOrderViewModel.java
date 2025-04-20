package com.example.foodapp.viewmodel.order;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.foodapp.dto.response.UserResponse;
import com.example.foodapp.repositories.OcrRepository;
import com.example.foodapp.repositories.UserRepository;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class AdminOrderViewModel extends ViewModel {

    private final MutableLiveData<List<UserResponse>> branchList = new MutableLiveData<>();
    private final MutableLiveData<String> ocrResult = new MutableLiveData<>();
    private final UserRepository userRepository;
    private final OcrRepository ocrRepository;

    public AdminOrderViewModel(Context context) {
        this.userRepository = new UserRepository(context);
        this.ocrRepository = new OcrRepository();
    }

    public LiveData<List<UserResponse>> getBranchList() {
        return branchList;
    }

    public LiveData<String> getOcrResult() {
        return ocrResult;
    }

    public void fetchAllBranch() {
        userRepository.getAllUsers(new UserRepository.UserListCallback() {
            @Override
            public void onSuccess(List<UserResponse> users) {
                branchList.setValue(users);
            }

            @Override
            public void onError(String message) {
                // handle error if needed
            }
        });
    }

    public void sendOrder(String branch, String content) {
        // implement actual sending logic here
    }

    public void performOCR(Uri uri, Context context) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            String base64Image = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);

            ocrRepository.sendToOCRSpace(base64Image, ocrResult);
        } catch (IOException e) {
            ocrResult.setValue("Failed to process image");
        }
    }
}