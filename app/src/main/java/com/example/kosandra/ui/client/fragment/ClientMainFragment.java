package com.example.kosandra.ui.client.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
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
import com.example.kosandra.ui.general_logic.ConfirmationDialog;
import com.example.kosandra.ui.general_logic.RvItemClickListener;
import com.example.kosandra.ui.general_logic.SearchRecyclerView;
import com.example.kosandra.view_model.ClientViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the main fragment of the client module, responsible for displaying and managing a list of clients.
 * <p>
 * This fragment extends Fragment and implements RvItemClickListener<Client> and SearchRecyclerView interfaces.
 */
public class ClientMainFragment extends Fragment implements RvItemClickListener<Client>, SearchRecyclerView {
    private FragmentMainClientBinding binding;// The binding object for the fragment layout
    private AdapterRVClientsMain adapter;// The adapter for the RecyclerView displaying clients
    private ClientViewModel viewModel;// The ViewModel for managing client data
    private int positionSelection = -1;// The position of selected client in the list
    private View prevItemLayout = null;// Reference to the previous selected item layout
    private View prevButDelete = null;// Reference to the previous selected delete button
    private List<Client> allClientList = new ArrayList<>(); // List of all clients

    private SearchView searchView;// The SearchView for filtering clients

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

    /**
     * Initializes the RecyclerView with the adapter.
     */
    private void initRecyclerView() {
        adapter = new AdapterRVClientsMain(this);
        binding.rvClient.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvClient.setAdapter(adapter);
    }

    /**
     * Observes changes in client data and updates the UI accordingly.
     */
    private void observeClients() {
        viewModel = new ViewModelProvider(requireActivity()).get(ClientViewModel.class);
        viewModel.getAllClients().observe(getViewLifecycleOwner(), clients -> {
            if (clients != null) {
                adapter.setClients(clients);
                allClientList = clients;
            }
        });
    }

    /**
     * Clears the binding object reference when the view is destroyed.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    /**
     * Collapses the search view when the fragment is stopped.
     */
    @Override
    public void onStop() {
        super.onStop();
        searchView.onActionViewCollapsed();
    }

    /**
     * Inflates the menu and sets menu items click listeners.
     *
     * @param menu         The menu object
     * @param menuInflater The menu inflater
     */
    @Override
    public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.menu_client_main, menu);
        MenuItem searchItem = menu.findItem(R.id.menu_client_search);
        searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);
    }

    /**
     * Handles menu item selection and sorts clients based on the selected option.
     *
     * @param menuItem The selected menu item
     * @return True if the item selection was handled, false otherwise
     */
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

    /**
     * Sorts clients by name in ascending order.
     */
    private void sortByClientNameAscending() {
        viewModel.getAllClientsSortedByNameAscending().observe(getViewLifecycleOwner(), clients -> {
            if (clients != null) {
                adapter.setClients(clients);
                allClientList = clients;
            }
        });
    }

    /**
     * Sorts clients by name in descending order.
     */
    private void sortByClientNameDescending() {
        viewModel.getAllClientsSortedByNameDescending().observe(getViewLifecycleOwner(), clients -> {
            if (clients != null) {
                adapter.setClients(clients);
                allClientList = clients;
            }
        });
    }

    /**
     * Sorts clients by visit count in ascending order.
     */
    private void sortByVisitCountAscending() {
        viewModel.getAllClientsSortedByVisitsAscending().observe(getViewLifecycleOwner(), clients -> {
            if (clients != null) {
                adapter.setClients(clients);
                allClientList = clients;
            }
        });
    }

    /**
     * Sorts clients by visit count in descending order.
     */
    private void sortByVisitCountDescending() {
        viewModel.getAllClientsSortedByVisitsDescending().observe(getViewLifecycleOwner(), clients -> {
            if (clients != null) {
                adapter.setClients(clients);
                allClientList = clients;
            }
        });
    }

    /**
     * Filters clients based on the text input and updates the adapter.
     *
     * @param text The text input used for filtering clients
     */
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

    /**
     * Handles the click event on a client item in the RecyclerView.
     *
     * @param item_layout The layout of the clicked item
     * @param but_delete  The delete button of the clicked item
     * @param client      The client object associated with the clicked item
     */
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

    /**
     * Handles the long click event on a client item in the RecyclerView.
     *
     * @param item_layout The layout of the long-clicked item
     * @param but_delete  The delete button of the long-clicked item
     * @param client      The client object associated with the long-clicked item
     */
    @Override
    public void onLongClick(View item_layout, View but_delete, Client client) {
        if (positionSelection == -1 || positionSelection == client.getId()) {
            positionSelection = client.getId();
            AnimationHelper.animateItemSelected(item_layout, but_delete);
            prevItemLayout = item_layout;
            prevButDelete = but_delete;
        } else if (prevItemLayout != null && prevButDelete != null) {
            positionSelection = client.getId();
            AnimationHelper.animateItemSelected(item_layout, but_delete);
            AnimationHelper.animateItemDeselected(prevItemLayout, prevButDelete);
            prevItemLayout = item_layout;
            prevButDelete = but_delete;
        }
    }

    /**
     * Handles the delete click event on a client item in the RecyclerView.
     *
     * @param item_layout The layout of the item to be deleted
     * @param but_delete  The delete button of the item
     * @param client      The client object to be deleted
     */
    @Override
    public void onDeleteClick(View item_layout, View but_delete, Client client) {
        openConfirmationDialog(item_layout, but_delete, client);
    }

    /**
     * Opens a confirmation dialog for deleting the client.
     */
    private void openConfirmationDialog(View item_layout, View but_delete, Client client) {
        ConfirmationDialog dialog = new ConfirmationDialog(
                getContext(),
                "Подтверждение",
                "Вы уверены, что хотите удалить клиента?",
                (dialogInterface, which) -> {
                    if (which == DialogInterface.BUTTON_POSITIVE) {
                        viewModel.delete(client);
                        AnimationHelper.cancelAnimation(item_layout, but_delete);
                        adapter.removeClient(client);
                        positionSelection = -1;
                        prevButDelete = null;
                        prevItemLayout = null;
                    } else if (which == DialogInterface.BUTTON_NEGATIVE) {
                    }
                });

        dialog.show();
    }

    /**
     * Handles the click event on a client's photo.
     *
     * @param client The client object associated with the photo
     * @param image  The image view clicked
     */
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