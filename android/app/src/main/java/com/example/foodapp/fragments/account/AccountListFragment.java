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
import com.example.foodapp.adapters.AdminAccountAdapter;
import com.example.foodapp.databinding.FragmentAccountListBinding;
import com.example.foodapp.dto.response.UserResponse;
import com.example.foodapp.enums.AccountListFunction;
import com.example.foodapp.enums.EditMode;
import com.example.foodapp.repositories.UserRepository;
import com.example.foodapp.utils.NavigationUtil;

import java.util.ArrayList;
import java.util.List;

public class AccountListFragment extends Fragment {

    private FragmentAccountListBinding binding;
    private AdminAccountAdapter adminAccountAdapter;
    private UserRepository userRepository;
    private final List<UserResponse> userList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAccountListBinding.inflate(inflater, container, false);
        userRepository = new UserRepository(requireContext());

        setupListeners();
        setupRecyclerView();
        fetchAccountList();

        return binding.getRoot();
    }

    private void setupListeners() {
        NavigationUtil.setupBackNavigation(this, binding.buttonBack);

        if (getArguments() != null) {
            AccountListFunction function = (AccountListFunction) getArguments().getSerializable("function");
            if (function != null) {
                switch (function) {
                    case ACCOUNT_LIST:
                        binding.newBranchAccount.setOnClickListener(v -> openCreateAccount());
                        adminAccountAdapter = new AdminAccountAdapter(requireContext(), userList, this::openEditAccount);
                        break;
                    case ORDER_HISTORY:
                        binding.newBranchAccount.setVisibility(View.GONE);
                        adminAccountAdapter = new AdminAccountAdapter(requireContext(), userList, this::openOrderHistory);
                        break;
                }
            }
        }
    }

    private void setupRecyclerView() {
        binding.accountRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.accountRecyclerView.setAdapter(adminAccountAdapter);
    }

    private void openEditAccount(UserResponse user) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("mode", EditMode.UPDATE);
        bundle.putSerializable("user", user);

        UpdateAccountFragment fragment = new UpdateAccountFragment();
        fragment.setArguments(bundle);
        openFragment(fragment);
    }

    private void openOrderHistory(UserResponse user) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("user", user);
        OrderHistoryFragment fragment = new OrderHistoryFragment();
        fragment.setArguments(bundle);
        openFragment(fragment);
    }

    private void openFragment(Fragment fragment) {
        getParentFragmentManager()
                .beginTransaction()
                .replace(R.id.accountContainer, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void openCreateAccount() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("mode", EditMode.CREATE);

        FragmentTransaction ft = getParentFragmentManager().beginTransaction();
        UpdateAccountFragment fragment = new UpdateAccountFragment();
        fragment.setArguments(bundle);
        ft.replace(R.id.accountContainer, fragment).addToBackStack(null).commit();
    }


    private void fetchAccountList() {
        userRepository.getAllUsers(new UserRepository.UserListCallback() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onSuccess(List<UserResponse> users) {
                userList.clear();
                userList.addAll(users);
                adminAccountAdapter.notifyDataSetChanged();
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
