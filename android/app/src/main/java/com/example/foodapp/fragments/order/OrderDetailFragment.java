package com.example.foodapp.fragments.order;

import android.app.AlertDialog;
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
import com.example.foodapp.enums.OrderStatus;
import com.example.foodapp.utils.NavigationUtil;
import com.example.foodapp.viewmodel.BaseViewModelFactory;
import com.example.foodapp.viewmodel.order.OrderDetailViewModel;

import java.util.ArrayList;

public class OrderDetailFragment extends Fragment {
    private FragmentOrderDetailBinding binding;
    private OrderDetailAdapter adapter;
    private OrderDetailViewModel viewModel;
    private OrderResponse currentOrder;

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
        viewModel = new ViewModelProvider(this, new BaseViewModelFactory<>(requireContext(), OrderDetailViewModel.class))
                .get(OrderDetailViewModel.class);

        setupRecyclerView();
        observeOrderData();
        loadData();
        setupActionButtons();
    }

    private void setupRecyclerView() {
        adapter = new OrderDetailAdapter(new ArrayList<>());
        binding.recyclerViewOrderHistoryDetail.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewOrderHistoryDetail.setAdapter(adapter);
    }

    private void loadData() {
        new Thread(() -> {
            OrderResponse order = null;
            if (getArguments() != null && getArguments().containsKey("order")) {
                order = (OrderResponse) getArguments().getSerializable("order");
            }
            if (order != null) {
                currentOrder = order;
                OrderResponse finalOrder = order;
                getActivity().runOnUiThread(() -> {
                    binding.tvTitle.setText("Order Detail " + finalOrder.getId());
                    viewModel.fetchOrderDetails(finalOrder.getId());
                    if (finalOrder.getStatus() == OrderStatus.PROCESSING) {
                        binding.layoutActions.setVisibility(View.VISIBLE);
                    } else {
                        binding.layoutActions.setVisibility(View.GONE);
                    }
                });
            } else {
                getActivity().runOnUiThread(() ->
                        Toast.makeText(getContext(), "Order not found", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    private void observeOrderData() {
        viewModel.getOrderDetails().observe(getViewLifecycleOwner(), orderDetails -> {
            adapter.setOrderDetails(orderDetails);
        });
    }

    private void setupActionButtons() {
        binding.btnDone.setOnClickListener(v -> {
            if (currentOrder != null) {
                viewModel.updateOrderStatus(currentOrder.getId(), OrderStatus.COMPLETED);
                Toast.makeText(getContext(), "Order marked as Completed", Toast.LENGTH_SHORT).show();
                getParentFragmentManager().popBackStack();
            }
        });

        binding.btnCancel.setOnClickListener(v -> {
            if (currentOrder != null) {
                new AlertDialog.Builder(requireContext())
                        .setTitle("Confirm Cancellation")
                        .setMessage("Are you sure you want to cancel this order?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            viewModel.updateOrderStatus(currentOrder.getId(), OrderStatus.CANCELLED);
                            Toast.makeText(getContext(), "Order canceled", Toast.LENGTH_SHORT).show();
                            getParentFragmentManager().popBackStack();
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
