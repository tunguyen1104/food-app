package com.example.foodapp.fragments.order;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.foodapp.adapters.OrderDetailAdapter;
import com.example.foodapp.databinding.FragmentOrderDetailBinding;
import com.example.foodapp.dto.response.OrderResponse;
import com.example.foodapp.utils.NavigationUtil;
import com.example.foodapp.viewmodel.order.OrderDetailViewModel;
import com.example.foodapp.viewmodel.order.OrderDetailViewModelFactory;

import java.util.ArrayList;

public class OrderDetailFragment extends Fragment {
    private FragmentOrderDetailBinding binding;
    private OrderDetailAdapter adapter;
    private OrderDetailViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentOrderDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        NavigationUtil.setupBackNavigation(this, binding.buttonBack);

        viewModel = new ViewModelProvider(this, new OrderDetailViewModelFactory(requireContext()))
                .get(OrderDetailViewModel.class);

        setupRecyclerView();
        observeOrderData();
        loadData();
    }

    private void setupRecyclerView() {
        adapter = new OrderDetailAdapter(new ArrayList<>());
        binding.recyclerViewOrderHistoryDetail.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewOrderHistoryDetail.setAdapter(adapter);
    }

    private void loadData() {
        if (getArguments() != null && getArguments().containsKey("order")) {
            OrderResponse order = (OrderResponse) getArguments().getSerializable("order");
            if (order != null) {
                binding.tvTitle.setText("Order Detail " + order.getId());
                viewModel.fetchOrderDetails(order.getId());
            } else {
                Toast.makeText(getContext(), "Order not found", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "Order not found", Toast.LENGTH_SHORT).show();
        }
    }

    private void observeOrderData() {
        viewModel.getOrderDetails().observe(getViewLifecycleOwner(), orderDetails -> {
            adapter.setOrderDetails(orderDetails);
        });
    }
}
