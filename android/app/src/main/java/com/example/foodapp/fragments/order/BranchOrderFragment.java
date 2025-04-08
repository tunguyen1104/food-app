package com.example.foodapp.fragments.order;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.foodapp.adapters.OrderAdapter;
import com.example.foodapp.components.OrderTagInfoDialog;
import com.example.foodapp.databinding.FragmentOrderBranchBinding;
import com.example.foodapp.dto.response.OrderResponse;
import com.example.foodapp.enums.OrderStatus;
import com.example.foodapp.viewmodel.BaseViewModelFactory;
import com.example.foodapp.viewmodel.order.BranchOrderViewModel;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
public class BranchOrderFragment extends Fragment {
    private FragmentOrderBranchBinding binding;
    private OrderAdapter orderAdapter;
    private BranchOrderViewModel branchOrderViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentOrderBranchBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Setup info button to show tag info dialog.
        binding.infoButton.setOnClickListener(v -> OrderTagInfoDialog.show(requireContext()));

        // Initialize BranchOrderViewModel.
        branchOrderViewModel = new ViewModelProvider(this,
                new BaseViewModelFactory<>(requireContext(), BranchOrderViewModel.class))
                .get(BranchOrderViewModel.class);

        // Setup RecyclerView.
        setupRecyclerView();
        // Setup TabLayout listener.
        setupTabLayout();

        branchOrderViewModel.fetchOrdersByStatus(OrderStatus.PROCESSING);

        observeOrderData();
    }

    private void setupRecyclerView() {
        orderAdapter = new OrderAdapter(requireContext(), new ArrayList<>(), this::openOrderDetail);
        binding.orderHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.orderHistoryRecyclerView.setAdapter(orderAdapter);
    }

    private void setupTabLayout() {
        binding.statusTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                OrderStatus status;
                switch (tab.getPosition()) {
                    case 0:
                        status = OrderStatus.PROCESSING;
                        break;
                    case 1:
                        status = OrderStatus.COMPLETED;
                        break;
                    case 2:
                        status = OrderStatus.CANCELLED;
                        break;
                    default:
                        status = OrderStatus.PROCESSING;
                        break;
                }
                branchOrderViewModel.fetchOrdersByStatus(status);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    private void openOrderDetail(OrderResponse order) {
    }

    private void observeOrderData() {
        branchOrderViewModel.getOrders().observe(getViewLifecycleOwner(), orders -> {
            if (orders != null) {
                orderAdapter.updateData(orders);
            } else {
                orderAdapter.updateData(new ArrayList<>());
            }
        });
    }
}