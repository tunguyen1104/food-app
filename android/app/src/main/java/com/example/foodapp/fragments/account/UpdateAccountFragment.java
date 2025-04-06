package com.example.foodapp.fragments.account;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.example.foodapp.R;
import com.example.foodapp.activities.MainActivity;
import com.example.foodapp.consts.Constants;
import com.example.foodapp.databinding.FragmentUpdateInfoBinding;
import com.example.foodapp.dto.request.CreateUserRequest;
import com.example.foodapp.dto.response.UserResponse;
import com.example.foodapp.enums.EditMode;
import com.example.foodapp.enums.EnableStatus;
import com.example.foodapp.repositories.UploadRepository;
import com.example.foodapp.repositories.UserRepository;
import com.example.foodapp.utils.AuthInterceptor;
import com.example.foodapp.utils.FileUtil;
import com.example.foodapp.utils.NavigationUtil;
import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class UpdateAccountFragment extends Fragment implements MainActivity.PermissionCallback {

    private FragmentUpdateInfoBinding binding;
    private UserRepository userRepository;
    private ActivityResultLauncher<Intent> cameraLauncher;
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private Uri selectedImageUri;
    private UploadRepository uploadRepository;
    private boolean isCameraPermitted = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentUpdateInfoBinding.inflate(inflater, container, false);
        userRepository = new UserRepository(requireContext());
        uploadRepository = new UploadRepository(requireContext());

        setupDropdownEnable();
        setupImagePicker();
        setupCamera();
        NavigationUtil.setupBackNavigation(this, binding.buttonBack);

        handleActionLayoutVisibility();

        return binding.getRoot();
    }

    private void loadUserIfAvailable() {
        UserResponse user = (UserResponse) getArguments().getSerializable("user");
        if (user == null) return;
        fillFormWithUserData(user);
        loadUserAvatar(user.getAvatarUrl());
    }
    private void handleActionLayoutVisibility() {
        EditMode mode = (EditMode) getArguments().getSerializable("mode");
        if (mode == EditMode.UPDATE) {
            binding.tvTitle.setText("Update Account");
            binding.layoutActions.setVisibility(View.VISIBLE);
            binding.addButton.setVisibility(View.GONE);
            loadUserIfAvailable();
            binding.btnSave.setOnClickListener(v -> updateAccount());
            binding.btnDelete.setOnClickListener(v -> deleteAccount());
        } else {
            binding.layoutActions.setVisibility(View.GONE);
            binding.addButton.setOnClickListener(v -> createAccount());
        }
    }

    private void fillFormWithUserData(UserResponse user) {
        binding.editTextFirstName.setText(extractFirstName(user.getFullName()));
        binding.editTextLastName.setText(extractLastName(user.getFullName()));
        binding.editTextPhoneNumber.setText(user.getPhone());
        binding.editUserName.setText(user.getUserName());
        binding.editTextLocation.setText(user.getLocation());

        EnableStatus status = (user.getEnabled() != null && user.getEnabled())
                ? EnableStatus.ON
                : EnableStatus.OFF;

        binding.enable.setText(status.getDisplayText(), false);
    }

    private void loadUserAvatar(String avatarUrl) {
        if (avatarUrl != null && !avatarUrl.isEmpty()) {
            Glide.with(requireContext())
                    .load(AuthInterceptor.getAuthorizedGlideUrl(Constants.URL_HOST_SERVER + avatarUrl))
                    .placeholder(R.drawable.avatar_default)
                    .into(binding.imageButtonAvatar);
        } else {
            binding.imageButtonAvatar.setImageResource(R.drawable.avatar_default);
        }
    }

    private String extractFirstName(String fullName) {
        if (fullName == null || fullName.isEmpty()) return "";
        String[] parts = fullName.trim().split("\\s+");
        return parts[parts.length - 1];
    }

    private String extractLastName(String fullName) {
        if (fullName == null || fullName.isEmpty()) return "";
        String[] parts = fullName.trim().split("\\s+");
        return String.join(" ", java.util.Arrays.copyOf(parts, parts.length - 1));
    }

    private void setupDropdownEnable() {
        String[] options = EnableStatus.getDisplayOptions();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, options);
        binding.enable.setAdapter(adapter);
        binding.enable.setText(EnableStatus.ON.getDisplayText(), false);
    }

    private void setupImagePicker() {
        imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                Uri uri = result.getData().getData();
                if (uri != null) setImageFromUri(uri);
            }
        });

        binding.imageButtonAvatar.setOnClickListener(v -> {
            requestStoragePermission();
            openImagePicker();
        });
    }

    private void setupCamera() {
        cameraLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                Bitmap bitmap = (Bitmap) result.getData().getExtras().get("data");
                binding.imageButtonAvatar.setImageBitmap(bitmap);
                selectedImageUri = getImageUriFromBitmap(bitmap);
            }
        });

        checkCameraPermission();
        binding.buttonCamera.setOnClickListener(v -> {
            openCamera();
            Toast.makeText(requireContext(), "Camera opened", Toast.LENGTH_SHORT).show();
        });
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imagePickerLauncher.launch(intent);
    }

    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraLauncher.launch(cameraIntent);
    }

    private Uri getImageUriFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(requireContext().getContentResolver(), bitmap, "Title", null);
        return Uri.parse(path);
    }

    private void setImageFromUri(Uri uri) {
        selectedImageUri = uri;
        Glide.with(requireContext()).load(uri).placeholder(com.example.foodapp.R.drawable.avatar_default).into(binding.imageButtonAvatar);
    }

    private void createAccount() {
        String firstName = binding.editTextFirstName.getText().toString().trim();
        String lastName = binding.editTextLastName.getText().toString().trim();
        String phone = binding.editTextPhoneNumber.getText().toString().trim();
        String userName = binding.editUserName.getText().toString().trim();
        String password = binding.editTextPassword.getText().toString().trim();
        String confirmPassword = binding.editTextConfirmPassword.getText().toString().trim();
        String location = binding.editTextLocation.getText().toString().trim();
        EnableStatus status = EnableStatus.fromDisplayText(binding.enable.getText().toString().trim());
        boolean enable = (status != null && status.getValue());

        if (selectedImageUri == null) {
            Toast.makeText(requireContext(), "Please fill all fields and select an image", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!isFormValid(firstName, lastName, phone, userName, password, confirmPassword, location)) {
            return;
        }

        uploadImageAndCreateUser(firstName, lastName, userName, phone, password, confirmPassword, location, enable);
    }

    private void uploadImageAndCreateUser(String firstName, String lastName, String userName, String phone,
                                          String password, String confirmPassword, String location, boolean enable) {
        File file = FileUtil.getFileFromUri(requireContext(), selectedImageUri);
        if (file == null) {
            Toast.makeText(requireContext(), "File not found", Toast.LENGTH_SHORT).show();
            return;
        }

        uploadRepository.uploadImage(file, new UploadRepository.UploadCallback() {
            @Override
            public void onSuccess(String imageUrl) {
                CreateUserRequest request = new CreateUserRequest(
                        firstName, lastName, userName, phone,
                        password, confirmPassword, location, enable, imageUrl
                );

                userRepository.createUser(request, new UserRepository.CreateUserCallback() {
                    @Override
                    public void onSuccess(UserResponse newUser) {
                        Toast.makeText(requireContext(), "Account created successfully!", Toast.LENGTH_SHORT).show();
                        getParentFragmentManager().popBackStack();
                    }

                    @Override
                    public void onError(String errorMessage) {
                        Toast.makeText(requireContext(), "Failed to create account: " + errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateAccount() {
        String firstName = binding.editTextFirstName.getText().toString().trim();
        String lastName = binding.editTextLastName.getText().toString().trim();
        String phone = binding.editTextPhoneNumber.getText().toString().trim();
        String userName = binding.editUserName.getText().toString().trim();
        String password = binding.editTextPassword.getText().toString().trim();
        String confirmPassword = binding.editTextConfirmPassword.getText().toString().trim();
        String location = binding.editTextLocation.getText().toString().trim();
        EnableStatus status = EnableStatus.fromDisplayText(binding.enable.getText().toString().trim());
        boolean enable = (status != null && status.getValue());

        if (!isFormValid(firstName, lastName, phone, userName, password, confirmPassword, location)) {
            return;
        }

        UserResponse currentUser = (UserResponse) getArguments().getSerializable("user");
        if (currentUser == null) {
            Toast.makeText(requireContext(), "User not found", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedImageUri != null) {
            File file = FileUtil.getFileFromUri(requireContext(), selectedImageUri);
            if (file == null) {
                Toast.makeText(requireContext(), "File not found", Toast.LENGTH_SHORT).show();
                return;
            }
            uploadRepository.uploadImage(file, new UploadRepository.UploadCallback() {
                @Override
                public void onSuccess(String imageUrl) {
                    updateAccountData(currentUser.getId(), firstName, lastName, phone, userName,
                            password, confirmPassword, location, enable, imageUrl);
                }

                @Override
                public void onError(String errorMessage) {
                    Toast.makeText(requireContext(), "Image upload failed: " + errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            updateAccountData(currentUser.getId(), firstName, lastName, phone, userName,
                    password, confirmPassword, location, enable, currentUser.getAvatarUrl());
        }
    }

    private void updateAccountData(Long userId, String firstName, String lastName, String phone,
                                   String userName, String password, String confirmPassword,
                                   String location, boolean enable, String avatarUrl) {
        CreateUserRequest requestData = new CreateUserRequest(
                firstName, lastName, userName, phone,
                password, confirmPassword, location, enable, avatarUrl
        );

        userRepository.updateUser(userId, requestData, new UserRepository.UpdateUserCallback() {
            @Override
            public void onSuccess(UserResponse updatedUser) {
                Toast.makeText(requireContext(), "Account updated successfully!", Toast.LENGTH_SHORT).show();
                getParentFragmentManager().popBackStack();
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(requireContext(), "Failed to update account: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isFormValid(String firstName, String lastName, String phone, String userName,
                                String password, String confirmPassword, String location) {
        if (TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName) || TextUtils.isEmpty(phone)
                || TextUtils.isEmpty(userName) || TextUtils.isEmpty(password)
                || TextUtils.isEmpty(confirmPassword) || TextUtils.isEmpty(location)) {
            Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!password.equals(confirmPassword)) {
            Toast.makeText(requireContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void deleteAccount() {
        UserResponse user = (UserResponse) getArguments().getSerializable("user");
        if (user == null) {
            Toast.makeText(requireContext(), "User not found", Toast.LENGTH_SHORT).show();
            return;
        }

        new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setTitle("Delete Account")
                .setMessage("Are you sure you want to delete this account?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    userRepository.deleteUser(user.getId(), new UserRepository.DeleteUserCallback() {
                        @Override
                        public void onSuccess(String message) {
                            Toast.makeText(requireContext(), "Account deleted successfully!", Toast.LENGTH_SHORT).show();
                            getParentFragmentManager().popBackStack();
                        }

                        @Override
                        public void onError(String errorMessage) {
                            Toast.makeText(requireContext(), "Failed to delete account: " + errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void checkCameraPermission() {
        ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(), this::onPermissionResult);

        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(android.Manifest.permission.CAMERA);
        } else {
            isCameraPermitted = true;
        }
    }

    private static final int STORAGE_PERMISSION_CODE = 100;

    private void requestStoragePermission() {
        String[] permissions;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions = new String[]{Manifest.permission.READ_MEDIA_IMAGES};
        } else {
            permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
        }

        boolean allGranted = true;
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(requireContext(), permission)
                    != PackageManager.PERMISSION_GRANTED) {
                allGranted = false;
                break;
            }
        }

        if (!allGranted) {
            ActivityCompat.requestPermissions(requireActivity(), permissions, STORAGE_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            boolean granted = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    granted = false;
                    break;
                }
            }

            if (granted) {
                Toast.makeText(getContext(), "Permission granted", Toast.LENGTH_SHORT).show();
                openImagePicker();
            } else {
                Toast.makeText(getContext(), "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onPermissionResult(boolean isGranted) {
        isCameraPermitted = isGranted;
        if (!isGranted) {
            Snackbar.make(requireView(), "Camera permission denied", Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
