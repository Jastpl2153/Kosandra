package com.example.kosandra.ui.client.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.kosandra.R;
import com.example.kosandra.databinding.FragmentClientCardBinding;
import com.example.kosandra.entity.Client;
import com.example.kosandra.entity.HairstyleVisit;
import com.example.kosandra.ui.client.adapter.AdapterRVClientHairstyle;
import com.example.kosandra.ui.client.dialogs.AddEditHairstyleDialog;
import com.example.kosandra.ui.general_logic.DatePickerHelperDialog;
import com.example.kosandra.ui.client.dialogs.EditCardClientDialog;
import com.example.kosandra.ui.client.client_listener_rv.ClientCardClickListener;
import com.example.kosandra.ui.general_logic.OpenImageFullScreen;
import com.example.kosandra.view_model.ClientViewModel;
import com.example.kosandra.view_model.HairstyleVisitViewModel;

public class ClientCardFragment extends Fragment implements ClientCardClickListener, OpenImageFullScreen {
    private Client client;
    private FragmentClientCardBinding binding;
    private AdapterRVClientHairstyle adapter;
    private HairstyleVisitViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentClientCardBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getClient();
        observeClient();
        initRVHairstyle();
        observeHairstyleVisits();
        initMenu();
        binding.clientImageCard.setOnClickListener(v -> openFullScreen(binding.clientImageCard.getDrawable(), getContext()));
    }

    private void getClient(){
        client = getArguments() != null ? getArguments().getParcelable("client") : null;
    }

    private void observeClient(){
        if (client != null) {
            ClientViewModel viewModel = new ViewModelProvider(this).get(ClientViewModel.class);
            viewModel.getClient(client.getId()).observe(getViewLifecycleOwner(), this::displayClientInfo);
        }
    }

    private void displayClientInfo(Client client) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(client.getPhoto(), 0, client.getPhoto().length);
        Drawable drawable =  new BitmapDrawable(getResources(), bitmap);
        binding.clientImageCard.setImageDrawable(drawable);
        binding.txClientName.setText(client.getName());
        binding.tvClientPhone.setText(client.getNumberPhone());
        binding.tvClientBirthday.setText(DatePickerHelperDialog.parseDateOutput(client.getDateOfBirth()));
        binding.tvHairLength.setText(String.valueOf(client.getHairLength()));
        binding.tvHairColor.setText(client.getHairColor());
        binding.tvHairDensity.setText(client.getHairDensity());
        binding.tvClientVisit.setText(String.valueOf(client.getNumberOfVisits()));
        binding.tvSpeakClient.setText(client.getConversationDetails());
    }

    private void initRVHairstyle() {
        adapter = new AdapterRVClientHairstyle(this);
        binding.rvVisit.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvVisit.setAdapter(adapter);
    }

    private void observeHairstyleVisits(){
        viewModel = new ViewModelProvider(requireActivity()).get(HairstyleVisitViewModel.class);
        viewModel.getAllClientHairstyles(client.getId()).observe(getViewLifecycleOwner(), visits -> {
            if (visits != null) {
                adapter.setVisitList(visits);
            }
        });
    }

    private void initMenu(){
        MenuProvider menuProvider = new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.menu_client_card, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.menu_edit){
                    editClient();
                    return true;
                }
                if (menuItem.getItemId() == R.id.menu_add_hairstyle){
                    addHairstyle();
                    return true;
                }
                return false;
            }
        };
        requireActivity().addMenuProvider(menuProvider, getViewLifecycleOwner());
    }

    private void editClient(){
        EditCardClientDialog editCardClientDialog = new EditCardClientDialog();
        editCardClientDialog.setArguments(initBundleClient());
        editCardClientDialog.show(getParentFragmentManager(), "DialogEditClient");
    }

    private void addHairstyle(){
        AddEditHairstyleDialog dialog = new AddEditHairstyleDialog();
        dialog.setArguments(initBundleClient());
        dialog.show(getParentFragmentManager(), "DialogAddHairstyle");
    }

    private Bundle initBundleClient() {
        Bundle arg = new Bundle();
        arg.putParcelable("client", client);
        arg.putString("logic", "add");
       return arg;
    }
    @Override
    public void onItemClick(View view, HairstyleVisit hairstyleVisit) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("visit", hairstyleVisit);
        bundle.putParcelable("client", client);
        Navigation.findNavController(view).navigate(R.id.action_clientCardFragment_to_hairstyleFragment, bundle);
    }
}