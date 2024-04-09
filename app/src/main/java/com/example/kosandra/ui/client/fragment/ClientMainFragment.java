package com.example.kosandra.ui.client.fragment;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.example.kosandra.R;
import com.example.kosandra.databinding.FragmentMainClientBinding;
import com.example.kosandra.entity.Client;
import com.example.kosandra.ui.animation.AnimationHelper;
import com.example.kosandra.ui.client.adapter.AdapterRVClientsMain;
import com.example.kosandra.ui.general_logic.RvItemClickListener;
import com.example.kosandra.ui.general_logic.SearchRecyclerView;
import com.example.kosandra.view_model.ClientViewModel;

import java.util.ArrayList;
import java.util.List;
public class ClientMainFragment extends Fragment implements RvItemClickListener<Client>, SearchRecyclerView {
    private FragmentMainClientBinding binding;
    private AdapterRVClientsMain adapter;
    private ClientViewModel viewModel;
    private int positionSelection = -1;
    private View prevItemLayout= null;
    private View prevButDelete = null;
    private List<Client> allClientList = new ArrayList<>();

    private SearchView searchView;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMainClientBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecyclerView();
        binding.butAddClient.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_navigation_open_add_client));
        observeClients();
        requireActivity().addMenuProvider(this, getViewLifecycleOwner(), Lifecycle.State.STARTED);
    }

    private void initRecyclerView() {
        adapter = new AdapterRVClientsMain(this);
        binding.rvClient.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvClient.setAdapter(adapter);
    }

    private void observeClients(){
        viewModel = new ViewModelProvider(requireActivity()).get(ClientViewModel.class);
        viewModel.getAllClients().observe(getViewLifecycleOwner(), clients -> {
            if (clients != null) {
                adapter.setClients(clients);
                allClientList = clients;
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onStop() {
        super.onStop();
        searchView.onActionViewCollapsed();
    }

    @Override
    public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.menu_client_main, menu);
        MenuItem searchItem = menu.findItem(R.id.menu_client_search);
        searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == R.id.menu_sort_client_name_asc) {
            sortByClientNameAscending();
            return true;
        } else if (itemId == R.id.menu_sort_name_client_desc) {
            sortByClientNameDescending();
            return true;
        } else if (itemId == R.id.menu_sort_visit_client_asc) {
            sortByVisitCountAscending();
            return true;
        } else if (itemId == R.id.menu_sort_visit_client_desc) {
            sortByVisitCountDescending();
            return true;
        }
        return false;
    }

    private void sortByClientNameAscending() {
        viewModel.getAllClientsSortedByNameAscending().observe(getViewLifecycleOwner(), clients -> {
            if (clients != null) {
                adapter.setClients(clients);
                allClientList = clients;
            }
        });
    }

    private void sortByClientNameDescending() {
        viewModel.getAllClientsSortedByNameDescending().observe(getViewLifecycleOwner(), clients -> {
            if (clients != null) {
                adapter.setClients(clients);
                allClientList = clients;
            }
        });
    }

    private void sortByVisitCountAscending() {
        viewModel.getAllClientsSortedByVisitsAscending().observe(getViewLifecycleOwner(), clients -> {
            if (clients != null) {
                adapter.setClients(clients);
                allClientList = clients;
            }
        });
    }

    private void sortByVisitCountDescending() {
        viewModel.getAllClientsSortedByVisitsDescending().observe(getViewLifecycleOwner(), clients -> {
            if (clients != null) {
                adapter.setClients(clients);
                allClientList = clients;
            }
        });
    }

    @Override
    public void filter(String text) {
        List<Client> filter = new ArrayList<>();
        for (Client client : allClientList) {
            if (client.getName().toLowerCase().contains(text.toLowerCase())) {
                filter.add(client);
            }
        }
        adapter.setClients(filter);
    }

    @Override
    public void onClick(View item_layout, View but_delete, Client client) {
        if (positionSelection == client.getId()) {
            AnimationHelper.animateItemDeselected(item_layout, but_delete);
            positionSelection = -1;
        } else {
            Bundle args = new Bundle();
            args.putParcelable("client", client);
            Navigation.findNavController(item_layout)
                    .navigate(R.id.action_navigation_open_card_client, args);
        }
    }

    @Override
    public void onLongClick(View item_layout, View but_delete, Client client) {
        if (positionSelection == -1 || positionSelection == client.getId()) {
            positionSelection = client.getId();
            AnimationHelper.animateItemSelected(item_layout, but_delete);
            prevItemLayout = item_layout;
            prevButDelete = but_delete;
        } else if (prevItemLayout != null && prevButDelete != null ){
            positionSelection = client.getId();
            AnimationHelper.animateItemSelected(item_layout, but_delete);
            AnimationHelper.animateItemDeselected(prevItemLayout, prevButDelete);
            prevItemLayout = item_layout;
            prevButDelete = but_delete;
        }
    }

    @Override
    public void onDeleteClick(View item_layout, View but_delete, Client client) {
        viewModel.delete(client);
        AnimationHelper.cancelAnimation(item_layout, but_delete);
    }

    @Override
    public void onPhotoClick(Client client, View image) {
        if (positionSelection != client.getId()) {
            Dialog dialog = new Dialog(image.getContext(), android.R.style.Theme_Material_Dialog_NoActionBar_MinWidth);
            dialog.setContentView(R.layout.dialog_client_photo_card);

            SubsamplingScaleImageView imageView = dialog.findViewById(R.id.photo);

            Bitmap bitmap = BitmapFactory.decodeByteArray(client.getPhoto(), 0, client.getPhoto().length);
            imageView.setImage(ImageSource.bitmap(bitmap));

            dialog.show();
        }
    }
}