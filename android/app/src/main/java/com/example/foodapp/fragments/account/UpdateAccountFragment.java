package com.example.foodapp.fragments.account;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.foodapp.activities.MainActivity;
import com.example.foodapp.databinding.FragmentUpdateInfoBinding;
import com.example.foodapp.dto.request.CreateUserRequest;
import com.example.foodapp.dto.response.UserResponse;
import com.example.foodapp.repositories.UserRepository;
import com.example.foodapp.utils.NavigationUtil;
import com.google.android.material.snackbar.Snackbar;

public class UpdateAccountFragment extends Fragment implements MainActivity.PermissionCallback{

    private FragmentUpdateInfoBinding binding;
    private UserRepository userRepository;
    private ActivityResultLauncher<Intent> cameraLauncher;
    private ActivityResultLauncher<Intent> imagePickerLauncher;

    private boolean isCameraPermitted = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentUpdateInfoBinding.inflate(inflater, container, false);
        userRepository = new UserRepository(requireContext());
        binding.addButton.setOnClickListener(v -> addAccount());

        NavigationUtil.setupBackNavigation(this, binding.buttonBack);

        // Set listener for ImageButton to open image picker
        binding.imageButtonAvatar.setOnClickListener(v -> openImagePicker());
        // Initialize the image picker launcher
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == getActivity().RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null && data.getData() != null) {
                            Uri imageUri = data.getData();
                            setImageFromUri(imageUri);
                        }
                    }
                }
        );

        // Setup camera launcher to capture an image
        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                        Bundle bundle = result.getData().getExtras();
                        Bitmap bitmap = (Bitmap) bundle.get("data");
                        binding.imageButtonAvatar.setImageBitmap(bitmap);
                    }
                }
        );

        // Set listener for camera button to open the camera
        checkCameraPermission();
        binding.buttonCamera.setOnClickListener(v -> {
            openCamera();
            Toast.makeText(requireContext(), "Camera opened", Toast.LENGTH_SHORT).show();
        });

        return binding.getRoot();
    }
    private void setImageFromUri(Uri imageUri) {
        Glide.with(requireContext())
                .load(imageUri)
                .placeholder(com.example.foodapp.R.drawable.avatar_default)
                .into(binding.imageButtonAvatar);
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imagePickerLauncher.launch(intent);
    }

    private void checkCameraPermission() {
        ActivityResultLauncher<String> requestPermissionLauncher =
                registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                    onPermissionResult(isGranted);
                });
        if (ContextCompat.checkSelfPermission(
                requireContext(), android.Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED) {
            isCameraPermitted = true;
        } else {
            requestPermissionLauncher.launch(android.Manifest.permission.CAMERA);
        }
    }

    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraLauncher.launch(cameraIntent);
    }

    private void addAccount() {
        String firstName = binding.editTextFirstName.getText().toString().trim();
        String lastName = binding.editTextLastName.getText().toString().trim();
        String phone = binding.editTextPhoneNumber.getText().toString().trim();
        String userName = binding.editUserName.getText().toString().trim();
        String password = binding.editTextPassword.getText().toString().trim();
        String confirmPassword = binding.editTextConfirmPassword.getText().toString().trim();
        String location = binding.editTextLocation.getText().toString().trim();
        String enable = binding.enable.toString().trim();

        if (TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName) || TextUtils.isEmpty(phone) ||
                TextUtils.isEmpty(userName) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword) || TextUtils.isEmpty(location)) {
            Toast.makeText(requireContext(), "Please fill all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(requireContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        CreateUserRequest request = new CreateUserRequest(
                firstName,
                lastName,
                userName,
                phone,
                password,
                confirmPassword,
                location,
                enable
        );

        userRepository.createUser(request, new UserRepository.CreateUserCallback() {
            @Override
            public void onSuccess(UserResponse newUser) {
                Toast.makeText(requireContext(), "Account created successfully!", Toast.LENGTH_SHORT).show();
                requireActivity().getSupportFragmentManager().popBackStack();
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(requireContext(), "Failed to create account: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onPermissionResult(boolean isGranted) {
        isCameraPermitted = isGranted;
        if (!isGranted) {
            Snackbar snackbar = Snackbar.make(requireView(), "Camera permission denied", Snackbar.LENGTH_SHORT);
            snackbar.show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
