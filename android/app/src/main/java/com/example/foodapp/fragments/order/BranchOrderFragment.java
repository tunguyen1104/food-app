package com.example.foodapp.fragments.order;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.foodapp.R;
import com.example.foodapp.adapters.OrderAdapter;
import com.example.foodapp.components.OrderTagInfoDialog;
import com.example.foodapp.databinding.FragmentOrderBranchBinding;
import com.example.foodapp.dto.response.OrderResponse;
import com.example.foodapp.enums.OrderDetailFunction;
import com.example.foodapp.enums.OrderStatus;
import com.example.foodapp.viewmodel.BaseViewModelFactory;
import com.example.foodapp.viewmodel.order.BranchOrderViewModel;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
public class BranchOrderFragment extends Fragment {
    private FragmentOrderBranchBinding binding;
    private OrderAdapter orderAdapter;
    private BranchOrderViewModel branchOrderViewModel;
    private OrderStatus currentStatus = OrderStatus.PROCESSING;
    private static final String KEY_CURRENT_STATUS = "currentStatus";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentOrderBranchBinding.inflate(inflater, container, false);

        if (savedInstanceState != null && savedInstanceState.containsKey(KEY_CURRENT_STATUS)) {
            currentStatus = (OrderStatus) savedInstanceState.getSerializable(KEY_CURRENT_STATUS);
        }
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.infoButton.setOnClickListener(v -> OrderTagInfoDialog.show(requireContext()));
        branchOrderViewModel = new ViewModelProvider(this,
                new BaseViewModelFactory<>(requireContext(), BranchOrderViewModel.class))
                .get(BranchOrderViewModel.class);

        setupRecyclerView();
        setupTabLayout();

        branchOrderViewModel.fetchOrdersByStatus(currentStatus);
        observeOrderData();
    }

    @Override
    public void onResume() {
        super.onResume();
        setSelectedTab(currentStatus);
        branchOrderViewModel.fetchOrdersByStatus(currentStatus);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(KEY_CURRENT_STATUS, currentStatus);
    }

    private void setupRecyclerView() {
        orderAdapter = new OrderAdapter(requireContext(), new ArrayList<>(), this::openOrderDetailHome);
        binding.orderHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.orderHistoryRecyclerView.setAdapter(orderAdapter);
    }

    private void setupTabLayout() {
        binding.statusTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        currentStatus = OrderStatus.PROCESSING;
                        break;
                    case 1:
                        currentStatus = OrderStatus.COMPLETED;
                        break;
                    case 2:
                        currentStatus = OrderStatus.CANCELLED;
                        break;
                    default:
                        currentStatus = OrderStatus.PROCESSING;
                        break;
                }
                branchOrderViewModel.fetchOrdersByStatus(currentStatus);
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                branchOrderViewModel.fetchOrdersByStatus(currentStatus);
            }
        });
    }

    private void openOrderDetailHome(OrderResponse order) {
        if (order == null) return;

        currentStatus = order.getStatus();

        Bundle bundle = new Bundle();
        bundle.putSerializable("order", order);
        OrderDetailFunction function;
        if (order.getStatus() == OrderStatus.COMPLETED) {
            function = OrderDetailFunction.ORDER_DETAIL_COMPLETED;
        } else if (order.getStatus() == OrderStatus.CANCELLED) {
            function = OrderDetailFunction.ORDER_DETAIL_CANCELLED;
        } else {
            function = OrderDetailFunction.ORDER_DETAIL_PROCESSING;
        }
        bundle.putSerializable("function", function);

        OrderDetailFragment fragment = new OrderDetailFragment();
        fragment.setArguments(bundle);

        FragmentTransaction ft = getParentFragmentManager().beginTransaction();
        ft.replace(R.id.orderContainer, fragment).addToBackStack(null).commit();
    }

    private void setSelectedTab(OrderStatus status) {
        int tabIndex = 0;
        switch (status) {
            case PROCESSING: tabIndex = 0; break;
            case COMPLETED: tabIndex = 1; break;
            case CANCELLED: tabIndex = 2; break;
        }
        if (binding.statusTab.getTabAt(tabIndex) != null) {
            binding.statusTab.getTabAt(tabIndex).select();
        }
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
