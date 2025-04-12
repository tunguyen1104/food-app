package com.example.foodapp.fragments.message;

import static com.example.foodapp.consts.Constants.ID_ADMIN_DEFAULT;

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
import com.example.foodapp.adapters.ContactAdapter;
import com.example.foodapp.databinding.FragmentContactListAdminBinding;
import com.example.foodapp.dto.response.ConversationResponse;
import com.example.foodapp.fragments.order.OrderDetailFragment;
import com.example.foodapp.listeners.OnContactClickListener;
import com.example.foodapp.viewmodel.BaseViewModelFactory;
import com.example.foodapp.viewmodel.message.ContactListViewModel;
import com.example.foodapp.viewmodel.message.SingleChatViewModel;

public class AdminListContactFragment extends Fragment {
    private FragmentContactListAdminBinding binding;
    private ContactListViewModel contactListViewModel;
    private ContactAdapter contactAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentContactListAdminBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        contactAdapter = new ContactAdapter(new OnContactClickListener() {
            @Override
            public void onContactClick(ConversationResponse conversation) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("conversation", conversation);
                SingleChatFragment fragment = new SingleChatFragment();
                fragment.setArguments(bundle);

                FragmentTransaction ft = getParentFragmentManager().beginTransaction();
                ft.replace(R.id.messageContainer, fragment).addToBackStack(null).commit();
            }
        });
        binding.contactRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.contactRecyclerView.setAdapter(contactAdapter);


        contactListViewModel = new ViewModelProvider(this,
                new BaseViewModelFactory<>(requireContext(), ContactListViewModel.class))
                .get(ContactListViewModel.class);

        contactListViewModel.loadAllConversationsForAdmin(ID_ADMIN_DEFAULT);

        // Quan sát LiveData danh sách conversation và cập nhật adapter
        contactListViewModel.getContacts().observe(getViewLifecycleOwner(), conversations -> {
            contactAdapter.setContacts(conversations);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
