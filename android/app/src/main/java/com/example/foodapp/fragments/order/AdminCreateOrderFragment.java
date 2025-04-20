package com.example.foodapp.fragments.order;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.foodapp.databinding.FragmentCreateOrderAdminBinding;
import com.example.foodapp.dto.response.UserResponse;
import com.example.foodapp.utils.NavigationUtil;
import com.example.foodapp.viewmodel.BaseViewModelFactory;
import com.example.foodapp.viewmodel.order.AdminOrderViewModel;

public class AdminCreateOrderFragment extends Fragment {
    private FragmentCreateOrderAdminBinding binding;
    private AdminOrderViewModel orderViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCreateOrderAdminBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Init ViewModel
        orderViewModel = new ViewModelProvider(this,
                new BaseViewModelFactory<>(requireContext(), AdminOrderViewModel.class))
                .get(AdminOrderViewModel.class);

        // Load list of branches and observe
        orderViewModel.getBranchList().observe(getViewLifecycleOwner(), branches -> {
            if (branches != null) {
                ArrayAdapter<UserResponse> adapter = new ArrayAdapter<>(
                        requireContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        branches
                );
                binding.listBranch.setAdapter(adapter);
            }
        });

        orderViewModel.getOcrResult().observe(getViewLifecycleOwner(), result ->
                binding.resultTextView.setText(result));

        orderViewModel.fetchAllBranch();

        // Back button
        NavigationUtil.setupBackNavigation(this, binding.buttonBack);

        binding.icScan.setOnClickListener(v -> openImageChooser());

        // Send button
        binding.btnSend.setOnClickListener(v -> {
            String selectedBranch = binding.listBranch.getText().toString();
            String orderContent = binding.resultTextView.getText().toString();

            if (selectedBranch.isEmpty() || orderContent.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            orderViewModel.sendOrder(selectedBranch, orderContent);
        });
    }

    private void openImageChooser() {
        imagePickerLauncher.launch("image/*");
    }

    private final ActivityResultLauncher<String> imagePickerLauncher =
        registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
            if (uri != null) {
                binding.icScan.setImageURI(uri);
                orderViewModel.performOCR(uri, requireContext());
            }
        });

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
