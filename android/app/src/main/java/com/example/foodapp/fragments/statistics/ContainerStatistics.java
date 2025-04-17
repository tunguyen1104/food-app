package com.example.foodapp.fragments.statistics;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.foodapp.adapters.StatisticsPagerAdapter;
import com.example.foodapp.databinding.ContainerStatisticBinding;
import com.example.foodapp.utils.NavigationUtil;
import com.example.foodapp.viewmodel.BaseViewModelFactory;
import com.example.foodapp.viewmodel.util.DateSelectionViewModel;
import com.google.android.material.tabs.TabLayoutMediator;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Locale;

public class ContainerStatistics extends Fragment {
    private ContainerStatisticBinding binding;
    private DateSelectionViewModel dateViewModel;
    private ArrayList<String> months;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = ContainerStatisticBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dateViewModel = new ViewModelProvider(requireActivity(), new BaseViewModelFactory<>(requireContext(), DateSelectionViewModel.class))
                .get(DateSelectionViewModel.class);

        setupMonthSpinner();
        setupViewPager();
        NavigationUtil.setupBackNavigation(this, binding.backButton);
    }

    private void setupMonthSpinner() {
        months = new ArrayList<>();
        LocalDate currentDate = LocalDate.now();
        int selectedYear = currentDate.getYear();
        int selectedMonth = currentDate.getMonthValue();

        for (int i = 1; i <= 12; i++) {
            LocalDate date = LocalDate.of(selectedYear, i, 1);
            String monthName = date.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault());
            months.add(monthName);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, months);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.monthSpinner.setAdapter(adapter);
        binding.monthSpinner.setSelection(selectedMonth - 1);

        binding.monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                dateViewModel.setSelectedMonth(position + 1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void setupViewPager() {
        StatisticsPagerAdapter pagerAdapter = new StatisticsPagerAdapter(requireActivity());
        binding.viewPager.setAdapter(pagerAdapter);

        new TabLayoutMediator(binding.tabLayout, binding.viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Order Statistics");
                    break;
                case 1:
                    tab.setText("Food Statistics");
                    break;
            }
        }).attach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
