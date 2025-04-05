package com.example.foodapp.fragments.account;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.foodapp.R;
import com.example.foodapp.adapters.admin.AccountAdapter;
import com.example.foodapp.databinding.FragmentAccountListBinding;
import com.example.foodapp.dto.response.UserResponse;
import com.example.foodapp.repositories.UserRepository;
import com.example.foodapp.utils.NavigationUtil;

import java.util.ArrayList;
import java.util.List;

public class AccountListFragment extends Fragment {

    private FragmentAccountListBinding binding;
    private AccountAdapter accountAdapter;
    private UserRepository userRepository;

    private final List<UserResponse> userList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAccountListBinding.inflate(inflater, container, false);
        userRepository = new UserRepository(requireContext());

        setupRecyclerView();
        fetchAccountList();

        // Handle back button
        NavigationUtil.setupBackNavigation(this, binding.buttonBack);

        binding.newBranchAccount.setOnClickListener(v -> {
            FragmentTransaction ft = getParentFragmentManager().beginTransaction();
            ft.replace(R.id.accountContainer, new UpdateAccountFragment()).addToBackStack(null).commit();
        });

        return binding.getRoot();
    }

    private void setupRecyclerView() {
        accountAdapter = new AccountAdapter(requireContext(), userList);
        binding.accountRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.accountRecyclerView.setAdapter(accountAdapter);
    }

    private void fetchAccountList() {
        userRepository.getAllUsers(new UserRepository.UserListCallback() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onSuccess(List<UserResponse> users) {
                userList.clear();
                userList.addAll(users);
                accountAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String message) {
                Toast.makeText(requireContext(), "Error loading accounts: " + message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
