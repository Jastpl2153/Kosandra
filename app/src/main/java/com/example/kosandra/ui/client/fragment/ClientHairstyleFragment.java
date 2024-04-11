package com.example.kosandra.ui.client.fragment;

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
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.kosandra.R;
import com.example.kosandra.databinding.FragmentClientHairstyleBinding;
import com.example.kosandra.db.KosandraDataBase;
import com.example.kosandra.entity.Client;
import com.example.kosandra.entity.HairstyleVisit;
import com.example.kosandra.entity.Materials;
import com.example.kosandra.ui.client.dialogs.AddEditHairstyleDialog;
import com.example.kosandra.ui.general_logic.ConfirmationDialog;
import com.example.kosandra.ui.general_logic.DatePickerHelperDialog;
import com.example.kosandra.ui.general_logic.OpenImageFullScreen;
import com.example.kosandra.view_model.ClientViewModel;
import com.example.kosandra.view_model.HairstyleVisitViewModel;
import com.example.kosandra.view_model.MaterialsViewModel;

import java.util.concurrent.Executors;

/**
 * ClientHairstyleFragment class represents a fragment for displaying and managing client's hairstyle information.
 * <p>
 * This class extends Fragment and implements OpenImageFullScreen interface.
 */
public class ClientHairstyleFragment extends Fragment implements OpenImageFullScreen {
    private FragmentClientHairstyleBinding binding;
    private HairstyleVisit hairstyleVisit;
    private HairstyleVisitViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentClientHairstyleBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getHairstyleVisit();
        initMenu();
        observeHairstyle();
        binding.imageCardHairstyle.setOnClickListener(v -> openFullScreen(binding.imageCardHairstyle.getDrawable(), getContext()));
    }

    /**
     * getHairstyleVisit method retrieves the HairstyleVisit object from the arguments passed to the fragment.
     */
    private void getHairstyleVisit() {
        hairstyleVisit = getArguments() != null ? getArguments().getParcelable("visit") : null;
    }

    /**
     * initMenu method initializes the menu for editing and deleting the hairstyle.
     */
    private void initMenu() {
        MenuProvider provider = new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.menu_client_hairstyle_card, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.menu_edit_hairstyle) {
                    editHairstyle();
                    return true;
                }
                if (menuItem.getItemId() == R.id.menu_delete_hairstyle) {
                    openConfirmationDialog();
                    return true;
                }
                return false;
            }
        };
        requireActivity().addMenuProvider(provider, getViewLifecycleOwner());
    }

    /**
     * editHairstyle method opens a dialog for editing the hairstyle.
     */
    private void editHairstyle() {
        AddEditHairstyleDialog dialog = new AddEditHairstyleDialog();
        dialog.setArguments(initBundleClient());
        dialog.show(getParentFragmentManager(), "DialogAddHairstyle");
    }

    /**
     * openConfirmationDialog method opens a confirmation dialog for deleting the hairstyle visit.
     */
    private void openConfirmationDialog() {
        ConfirmationDialog dialog = new ConfirmationDialog(
                getContext(),
                "Подтверждение",
                "Вы уверены, что хотите удалить посещение?",
                (dialogInterface, which) -> {
                    if (which == DialogInterface.BUTTON_POSITIVE) {
                        deleteHairstyle();
                    } else if (which == DialogInterface.BUTTON_NEGATIVE) {
                        dialogInterface.dismiss();
                    }
                });

        dialog.show();
    }

    /**
     * deleteHairstyle method deletes the current hairstyle visit and updates the UI accordingly.
     */
    private void deleteHairstyle() {
        Executors.newSingleThreadExecutor().execute(() -> {
            KosandraDataBase.getInstance(requireContext()).runInTransaction(() -> {
                viewModel.delete(hairstyleVisit);
                minusCountVisitClient();
                returnValueCountMaterial();
            });
        });
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
        navController.popBackStack(R.id.clientCardFragment, false);
    }

    /**
     * observeHairstyle method observes changes in the hairstyle visit and updates the UI accordingly.
     */
    private void observeHairstyle() {
        viewModel = new ViewModelProvider(requireActivity()).get(HairstyleVisitViewModel.class);
        viewModel.getHairstyleVisit(hairstyleVisit.getId()).observe(getViewLifecycleOwner(), hairstyleVisit1 -> initSetField());
    }

    /**
     * initSetField method initializes and sets the UI fields with the data from the current hairstyle visit.
     */
    private void initSetField() {
        if (hairstyleVisit != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(hairstyleVisit.getPhotoHairstyle(), 0, hairstyleVisit.getPhotoHairstyle().length);
            binding.imageCardHairstyle.setImageBitmap(bitmap);
            binding.nameCardHairstyle.setText(hairstyleVisit.getHaircutName());
            binding.dateCardHairstyle.setText(DatePickerHelperDialog.parseDateOutput(hairstyleVisit.getVisitDate()));
            binding.timeCardHairstyle.setText(hairstyleVisit.getTimeSpent().toString());
            binding.priceCardHairstyle.setText(String.valueOf(hairstyleVisit.getHaircutCost()));
            binding.priceMaterialCardHairstyle.setText(String.valueOf(hairstyleVisit.getMaterialCost()));
            binding.weightCardHairstyle.setText(String.valueOf(hairstyleVisit.getMaterialWeight()));
            initSetUseMaterials();
        }
    }

    /**
     * initSetUseMaterials method initializes and sets the UI with the materials used in the hairstyle visit.
     */
    private void initSetUseMaterials() {
        for (int i = 0; i < hairstyleVisit.getCodeMaterial().length; i++) {
            View view = LayoutInflater.from(requireContext()).inflate(R.layout.item_client_card_hairstyle_material, binding.layoutCardHairstyle, false);
            TextView code = view.findViewById(R.id.card_hairstyle_use_material_code);
            TextView count = view.findViewById(R.id.card_hairstyle_use_material_count);

            code.setText(String.format("%s %s", code.getText().toString(), hairstyleVisit.getCodeMaterial()[i]));
            count.setText(String.valueOf(hairstyleVisit.getCountMaterial()[i]));

            binding.layoutCardHairstyle.addView(view);
        }
    }

    /**
     * minusCountVisitClient method decrements the number of visits for the current client.
     */
    private void minusCountVisitClient() {
        Client client = getArguments() != null ? getArguments().getParcelable("client") : null;
        ClientViewModel clientViewModel = new ViewModelProvider(requireActivity()).get(ClientViewModel.class);
        assert client != null;
        if (client.getNumberOfVisits() > 0) {
            client.setNumberOfVisits(client.getNumberOfVisits() - 1);
            clientViewModel.update(client);
        }
    }

    /**
     * returnValueCountMaterial method updates the count of materials used in the hairstyle visit.
     */
    private void returnValueCountMaterial() {
        MaterialsViewModel materialsViewModel = new ViewModelProvider(requireActivity()).get(MaterialsViewModel.class);
        int countMaterials = hairstyleVisit.getCountMaterial().length;
        for (int i = 0; i < countMaterials; i++) {
            LinearLayout linearLayout = (LinearLayout) binding.layoutCardHairstyle.getChildAt(binding.layoutCardHairstyle.getChildCount() - countMaterials + i);
            TextView countUse = linearLayout.findViewById(R.id.card_hairstyle_use_material_count);
            Materials material = materialsViewModel.getMaterial(hairstyleVisit.getCodeMaterial()[i].trim());
            material.setCount(material.getCount() + Integer.parseInt(countUse.getText().toString()));
            materialsViewModel.update(material);
        }
    }

    /**
     * initBundleClient method initializes a Bundle with the necessary data for editing the client's hairstyle.
     *
     * @return Bundle containing the hairstyle visit and logic for editing.
     */
    private Bundle initBundleClient() {
        Bundle arg = new Bundle();
        arg.putParcelable("hairstyle", hairstyleVisit);
        arg.putString("logic", "edit");
        return arg;
    }
}