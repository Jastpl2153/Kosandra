package com.example.kosandra.ui.client;

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
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.kosandra.R;
import com.example.kosandra.databinding.FragmentClientCardBinding;
import com.example.kosandra.entity.Client;
import com.example.kosandra.entity.HairstyleVisit;
import com.example.kosandra.ui.client.adapter.AdapterRVListVisitClient;
import com.example.kosandra.ui.client.dialogs.AddHairstyleDialog;
import com.example.kosandra.ui.client.dialogs.EditClientDialog;
import com.example.kosandra.ui.client.interface_recycle_view.ClientCardClickListener;
import com.example.kosandra.view_model.ClientViewModel;
import com.example.kosandra.view_model.HairstyleVisitViewModel;

public class ClientCardFragment extends Fragment implements ClientCardClickListener {
    private Client client;
    private FragmentClientCardBinding binding;
    private AdapterRVListVisitClient adapter;
    private HairstyleVisitViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentClientCardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        setupMethod();
        return root;
    }

    private void setupMethod() {
        getClient();
        initRecyclerView();
        observeHairstyleVisits();
        setupMenu();
    }
    private void getClient(){
        client = getArguments() != null ? getArguments().getParcelable("client") : null;
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
        binding.tvClientBirthday.setText(client.getDateOfBirth().toString());
        binding.tvHairLength.setText(String.valueOf(client.getHairLength()));
        binding.tvHairColor.setText(client.getHairColor());
        binding.tvHairDensity.setText(client.getHairDensity());
        binding.tvClientVisit.setText(String.valueOf(client.getNumberOfVisits()));
        binding.tvSpeakClient.setText(client.getConversationDetails());
    }
    private void initRecyclerView() {
        viewModel = new ViewModelProvider(requireActivity()).get(HairstyleVisitViewModel.class);
        adapter = new AdapterRVListVisitClient(this);
        binding.rvVisit.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvVisit.setAdapter(adapter);
    }
    private void observeHairstyleVisits(){
        viewModel.getAllHairstyleClient(client.getId()).observe(getViewLifecycleOwner(), visits -> {
            if (visits != null) {
                adapter.setVisitList(visits);
            }
        });
    }
    private void setupMenu (){
        MenuProvider menuProvider = new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.menu_card_client, menu);
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
        EditClientDialog editClientDialog = new EditClientDialog();
        editClientDialog.setArguments(initBundleClient());
        editClientDialog.show(getParentFragmentManager(), "DialogEditClient");
    }
    private void addHairstyle(){
        AddHairstyleDialog dialog = new AddHairstyleDialog();
        dialog.setArguments(initBundleClient());
        dialog.show(getParentFragmentManager(), "DialogAddHairstyle");
    }
    private Bundle initBundleClient() {
        Bundle arg = new Bundle();
        arg.putParcelable("client", client);
       return arg;
    }
    @Override
    public void onItemClick(View view, HairstyleVisit hairstyleVisit) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("visit", hairstyleVisit);
        Navigation.findNavController(view).navigate(R.id.action_clientCardFragment_to_hairstyleFragment, bundle);
    }
}