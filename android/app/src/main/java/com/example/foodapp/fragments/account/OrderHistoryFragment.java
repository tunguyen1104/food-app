package com.example.foodapp.fragments.account;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.foodapp.R;
import com.example.foodapp.adapters.admin.OrderAdapter;
import com.example.foodapp.databinding.FragmentOrderHistoryBinding;
import com.example.foodapp.dto.response.OrderResponse;
import com.example.foodapp.dto.response.UserResponse;
import com.example.foodapp.fragments.order.OrderDetailFragment;
import com.example.foodapp.utils.NavigationUtil;
import com.example.foodapp.viewmodel.order.OrderHistoryViewModel;
import com.example.foodapp.viewmodel.order.OrderHistoryViewModelFactory;
import java.util.ArrayList;
public class OrderHistoryFragment extends Fragment {

    private FragmentOrderHistoryBinding binding;
    private OrderAdapter orderAdapter;
    private OrderHistoryViewModel orderHistoryViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentOrderHistoryBinding.inflate(inflater, container, false);

        orderHistoryViewModel = new ViewModelProvider(this, new OrderHistoryViewModelFactory(requireContext())).get(OrderHistoryViewModel.class);
        NavigationUtil.setupBackNavigation(this, binding.buttonBack);

        setupRecyclerView();

        loadData();

        observeOrderData();

        return binding.getRoot();
    }

    private void setupRecyclerView() {
        orderAdapter = new OrderAdapter(requireContext(), new ArrayList<>(), this::openOrderDetail);
        binding.orderHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.orderHistoryRecyclerView.setAdapter(orderAdapter);
    }

    private void openOrderDetail(OrderResponse order) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("order", order);

        OrderDetailFragment fragment = new OrderDetailFragment();
        fragment.setArguments(bundle);

        FragmentTransaction ft = getParentFragmentManager().beginTransaction();
        ft.replace(R.id.accountContainer, fragment).addToBackStack(null).commit();
    }


    private void loadData() {
        UserResponse user = (UserResponse) getArguments().getSerializable("user");
        if (user != null) {
            binding.tvTitle.setText(user.getFullName());
            orderHistoryViewModel.fetchOrders(user.getId());
        } else {
            Toast.makeText(requireContext(), "User not found", Toast.LENGTH_SHORT).show();
        }
    }

    private void observeOrderData() {
        orderHistoryViewModel.getOrders().observe(getViewLifecycleOwner(), orders -> {
            if (orders != null) {
                orderAdapter.updateData(orders);
            }
        });
    }
}
