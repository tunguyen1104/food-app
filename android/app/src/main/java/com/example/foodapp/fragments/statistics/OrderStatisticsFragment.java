package com.example.foodapp.fragments.statistics;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.foodapp.databinding.FragmentOrderStatisticsBinding;
import com.example.foodapp.dto.response.DailyVolumeResponse;
import com.example.foodapp.dto.response.MonthlyRevenueResponse;
import com.example.foodapp.dto.response.OrderStatusResponse;
import com.example.foodapp.dto.response.PlatformRevenueResponse;
import com.example.foodapp.viewmodel.BaseViewModelFactory;
import com.example.foodapp.viewmodel.order.OrderStatisticsViewModel;
import com.example.foodapp.viewmodel.util.DateSelectionViewModel;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class OrderStatisticsFragment extends Fragment {
    private FragmentOrderStatisticsBinding binding;
    private OrderStatisticsViewModel viewModel;
    private DateSelectionViewModel dateViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentOrderStatisticsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity(), new BaseViewModelFactory<>(requireContext(), OrderStatisticsViewModel.class))
                .get(OrderStatisticsViewModel.class);
        dateViewModel = new ViewModelProvider(requireActivity(), new BaseViewModelFactory<>(requireContext(), DateSelectionViewModel.class))
                .get(DateSelectionViewModel.class);

        setupCharts();
        observeDateChanges();
    }

    private void setupCharts() {
        binding.revenueBarChart.getDescription().setEnabled(false);
        binding.revenueBarChart.setFitBars(true);

        binding.statusPieChart.getDescription().setEnabled(false);
        binding.statusPieChart.setUsePercentValues(true);
        binding.statusPieChart.setDrawHoleEnabled(true);

        binding.platformBarChart.getDescription().setEnabled(false);
        binding.platformBarChart.setFitBars(true);

        binding.volumeLineChart.getDescription().setEnabled(false);
        binding.volumeLineChart.setDrawGridBackground(false);
    }

    private void observeDateChanges() {
        dateViewModel.getSelectedYear().observe(getViewLifecycleOwner(), year -> {
            Integer month = dateViewModel.getSelectedMonth().getValue();
            if (month != null) {
                loadData(year, month);
            }
        });

        dateViewModel.getSelectedMonth().observe(getViewLifecycleOwner(), month -> {
            Integer year = dateViewModel.getSelectedYear().getValue();
            if (year != null) {
                loadData(year, month);
            }
        });
    }

    private void loadData(int year, int month) {
        viewModel.getMonthlyRevenue(year, month).observe(getViewLifecycleOwner(), revenueResponses -> {
            if (revenueResponses != null) {
                updateRevenueChart(revenueResponses);
            }
        });

        viewModel.getOrderStatusDistribution(year, month).observe(getViewLifecycleOwner(), statusResponses -> {
            if (statusResponses != null) {
                updateStatusChart(statusResponses);
            }
        });

        viewModel.getPlatformRevenue(year, month).observe(getViewLifecycleOwner(), platformResponses -> {
            if (platformResponses != null) {
                updatePlatformChart(platformResponses);
            }
        });

        viewModel.getDailyVolume(year, month).observe(getViewLifecycleOwner(), volumeResponses -> {
            if (volumeResponses != null) {
                updateVolumeChart(volumeResponses);
            }
        });
    }

    private void updateRevenueChart(List<MonthlyRevenueResponse> revenueResponses) {
        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();

        for (int i = 0; i < revenueResponses.size(); i++) {
            MonthlyRevenueResponse response = revenueResponses.get(i);
            entries.add(new BarEntry(i, (float) response.getRevenue()));
            labels.add(response.getMonth());
        }

        BarDataSet dataSet = new BarDataSet(entries, "Revenue");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        BarData barData = new BarData(dataSet);
        binding.revenueBarChart.setData(barData);
        binding.revenueBarChart.getXAxis().setValueFormatter(new com.github.mikephil.charting.formatter.IndexAxisValueFormatter(labels));
        binding.revenueBarChart.invalidate();
    }

    private void updateStatusChart(List<OrderStatusResponse> statusResponses) {
        ArrayList<PieEntry> entries = new ArrayList<>();
        for (OrderStatusResponse response : statusResponses) {
            entries.add(new PieEntry(response.getCount(), response.getStatus()));
        }

        PieDataSet dataSet = new PieDataSet(entries, "Order Status");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        PieData pieData = new PieData(dataSet);
        binding.statusPieChart.setData(pieData);
        binding.statusPieChart.invalidate();
    }

    private void updatePlatformChart(List<PlatformRevenueResponse> platformResponses) {
        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();

        for (int i = 0; i < platformResponses.size(); i++) {
            PlatformRevenueResponse response = platformResponses.get(i);
            entries.add(new BarEntry(i, (float) response.getRevenue()));
            labels.add(response.getPlatform());
        }

        BarDataSet dataSet = new BarDataSet(entries, "Platform Revenue");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        BarData barData = new BarData(dataSet);
        binding.platformBarChart.setData(barData);
        binding.platformBarChart.getXAxis().setValueFormatter(new com.github.mikephil.charting.formatter.IndexAxisValueFormatter(labels));
        binding.platformBarChart.invalidate();
    }

    private void updateVolumeChart(List<DailyVolumeResponse> volumeResponses) {
        ArrayList<Entry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();

        for (DailyVolumeResponse response : volumeResponses) {
            entries.add(new Entry(response.getDay(), response.getOrderCount()));
            labels.add(String.valueOf(response.getDay()));
        }

        LineDataSet dataSet = new LineDataSet(entries, "Daily Orders");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setDrawCircles(true);
        dataSet.setCircleColors(ColorTemplate.MATERIAL_COLORS);

        LineData lineData = new LineData(dataSet);
        binding.volumeLineChart.setData(lineData);
        binding.volumeLineChart.getXAxis().setValueFormatter(new com.github.mikephil.charting.formatter.IndexAxisValueFormatter(labels));
        binding.volumeLineChart.invalidate();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
