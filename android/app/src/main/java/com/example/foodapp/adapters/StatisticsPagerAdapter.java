package com.example.foodapp.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.foodapp.fragments.statistics.FoodStatisticsFragment;
import com.example.foodapp.fragments.statistics.OrderStatisticsFragment;

public class StatisticsPagerAdapter extends FragmentStateAdapter {
    public StatisticsPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new OrderStatisticsFragment();
            case 1:
                return new FoodStatisticsFragment();
            default:
                return new OrderStatisticsFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
