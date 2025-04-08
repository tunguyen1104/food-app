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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.foodapp.R;
import com.example.foodapp.adapters.AdminAccountAdapter;
import com.example.foodapp.databinding.FragmentAccountListBinding;
import com.example.foodapp.dto.response.UserResponse;
import com.example.foodapp.enums.AccountListFunction;
import com.example.foodapp.enums.EditMode;
import com.example.foodapp.utils.NavigationUtil;
import com.example.foodapp.viewmodel.account.AccountListViewModel;
import com.example.foodapp.viewmodel.account.AccountListViewModelFactory;

import java.util.ArrayList;
import java.util.List;

public class AccountListFragment extends Fragment {

    private FragmentAccountListBinding binding;
    private AdminAccountAdapter adminAccountAdapter;
    private final List<UserResponse> userList = new ArrayList<>();
    private AccountListViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAccountListBinding.inflate(inflater, container, false);

        viewModel = new ViewModelProvider(this, new AccountListViewModelFactory(requireContext()))
                .get(AccountListViewModel.class);

        setupListeners();
        setupRecyclerView();
        observeUserList();
        viewModel.fetchAllUsers();

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

    private void observeUserList() {
        viewModel.getUserList().observe(getViewLifecycleOwner(), users -> {
            userList.clear();
            if (users != null) {
                userList.addAll(users);
                adminAccountAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(requireContext(), "Error loading accounts", Toast.LENGTH_SHORT).show();
            }
        });
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

    private void openCreateAccount() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("mode", EditMode.CREATE);

        UpdateAccountFragment fragment = new UpdateAccountFragment();
        fragment.setArguments(bundle);

        getParentFragmentManager()
                .beginTransaction()
                .replace(R.id.accountContainer, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void openFragment(Fragment fragment) {
        getParentFragmentManager()
                .beginTransaction()
                .replace(R.id.accountContainer, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
