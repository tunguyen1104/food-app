package com.example.foodapp.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.foodapp.fragments.account.AccountContainer;
import com.example.foodapp.fragments.home.HomeContainer;
import com.example.foodapp.fragments.message.MessageContainer;
import com.example.foodapp.fragments.order.BranchOrderFragment;

public class ScreenSlidePagerAdapter extends FragmentStateAdapter {
    public ScreenSlidePagerAdapter(FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 1:
                return new BranchOrderFragment();
            case 2:
                return new MessageContainer();
            case 3:
                return new AccountContainer();
            default:
                return new HomeContainer();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
