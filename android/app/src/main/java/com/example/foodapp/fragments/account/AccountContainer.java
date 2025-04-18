package com.example.foodapp.fragments.account;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.foodapp.R;
import com.example.foodapp.dto.response.UserResponse;
import com.example.foodapp.enums.Role;
import com.example.foodapp.utils.UserManager;

public class AccountContainer extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.container_account, container, false);
        // why? https://stackoverflow.com/questions/7508044/android-fragment-no-view-found-for-id
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        UserResponse currentUser = UserManager.getUser(requireContext());
        if(currentUser != null && currentUser.getRole().equals(Role.MANAGER.toString())) {
            ft.add(R.id.accountContainer, new AdminAccountFragment()).commit();
        } else ft.add(R.id.accountContainer, new BranchAccountFragment()).commit();

        return view;
    }
}
