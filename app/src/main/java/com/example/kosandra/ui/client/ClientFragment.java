package com.example.kosandra.ui.client;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.kosandra.R;
import com.example.kosandra.databinding.FragmentClientBinding;
import com.example.kosandra.view_model.ClientViewModel;

import java.util.List;

public class ClientFragment extends Fragment {
    private FragmentClientBinding binding;
    private AdapterRecyclerView adapter;
    private ClientViewModel clientViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentClientBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        initRecyclerView();
        listenerAddClient();
        getAllClientAdapter();

        return root;
    }

    private void initRecyclerView() {
        clientViewModel = new ViewModelProvider(requireActivity()).get(ClientViewModel.class);
        adapter = new AdapterRecyclerView(clientViewModel);
        binding.rvClient.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvClient.setAdapter(adapter);
    }

    private void listenerAddClient(){
        binding.butAddClient.setOnClickListener(
                v -> Navigation.findNavController(v)
                        .navigate(R.id.action_navigation_open_add_client));
    }

    private void getAllClientAdapter(){
        clientViewModel.getAllClients().observe(getViewLifecycleOwner(), clients -> {
            if (clients != null) {
                adapter.setClients(clients);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}