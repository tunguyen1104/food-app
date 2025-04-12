package com.example.foodapp.fragments.message;

import static com.example.foodapp.consts.Constants.ID_ADMIN_DEFAULT;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.foodapp.R;
import com.example.foodapp.dto.response.UserResponse;
import com.example.foodapp.utils.UserManager;

public class MessageContainer extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.container_message, container, false);
        // why? https://stackoverflow.com/questions/7508044/android-fragment-no-view-found-for-id
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        UserResponse currentUser = UserManager.getUser(requireContext());
        if(currentUser != null && currentUser.getId().toString().equals(ID_ADMIN_DEFAULT)) {
            ft.add(R.id.messageContainer, new AdminListContactFragment()).commit();
        } else ft.add(R.id.messageContainer, new SingleChatFragment()).commit();

        return view;
    }
}

