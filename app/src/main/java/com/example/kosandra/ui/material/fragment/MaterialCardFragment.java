package com.example.kosandra.ui.material.fragment;

import android.content.DialogInterface;
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
import androidx.gridlayout.widget.GridLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.kosandra.R;
import com.example.kosandra.databinding.FragmentMaterialCardBinding;
import com.example.kosandra.entity.Materials;
import com.example.kosandra.ui.general_logic.ConfirmationDialog;
import com.example.kosandra.ui.general_logic.OpenImageFullScreen;
import com.example.kosandra.ui.material.dialog.EditCardMaterialDialog;
import com.example.kosandra.view_model.MaterialsViewModel;

/**
 * MaterialCardFragment class extends Fragment and implements OpenImageFullScreen interface
 * <p>
 * This fragment is responsible for displaying detailed information about a specific material
 */
public class MaterialCardFragment extends Fragment implements OpenImageFullScreen {
    private FragmentMaterialCardBinding binding;
    private Materials material;
    MaterialsViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMaterialCardBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getMaterial();
        observeMaterial();
        initMenu();
        binding.imageCardMaterial.setOnClickListener(v -> openFullScreen(binding.imageCardMaterial.getDrawable(), getContext()));
    }

    /**
     * Retrieves the material object from arguments
     * Sets the material field or null if no arguments are present
     */
    private void getMaterial() {
        material = getArguments() != null ? getArguments().getParcelable("materials") : null;
    }

    /**
     * Observes changes in the material object
     * Initializes ViewModel and sets up data observables
     */
    private void observeMaterial() {
        if (material != null) {
            viewModel = new ViewModelProvider(this).get(MaterialsViewModel.class);
            viewModel.getMaterial(material.getId()).observe(getViewLifecycleOwner(), this::initSetFields);
        }
    }

    /**
     * Initializes views with material information
     * Populates views based on the type of material
     *
     * @param materials Material object
     */
    private void initSetFields(Materials materials) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(materials.getPhoto(), 0, materials.getPhoto().length);
        Drawable drawable = new BitmapDrawable(getResources(), bitmap);
        binding.imageCardMaterial.setImageDrawable(drawable);
        binding.typeMaterialInfo.setText(materials.getTypeMaterials());
        binding.colorMaterialInfo.setText(materials.getColorMaterial());
        binding.codeMaterialInfo.setText(materials.getCodeMaterial());
        binding.countMaterialInfo.setText(String.valueOf(materials.getCount()));
        binding.priceMaterialInfo.setText(String.valueOf(materials.getCost()));

        switch (materials.getTypeMaterials()) {
            case "Канекалон":
                populateMaterialInfo(materials, 6, new int[]{7, 8, 16, 17});
                break;
            case "Кудри":
                populateMaterialInfo(materials, 5, new int[]{5, 6, 14, 15});
                break;
            case "Термоволокно":
                hideMaterialInfo(new int[]{5, 6, 7, 8, 14, 15, 16, 17});
                break;
        }
    }

    /**
     * Populates the material information based on the provided Materials object, padding index, and array of hidden view indices.
     * <p>
     * Sets padding, hides specified views, and displays material information based on the type of material.
     *
     * @param materials         The Materials object containing material information
     * @param paddingChildIndex The index of the child view in the grid layout to set padding
     * @param hiddenViewIndices Array of indices of views in the grid layout to hide
     */
    private void populateMaterialInfo(Materials materials, int paddingChildIndex, int[] hiddenViewIndices) {
        binding.gridLayoutCardInfo.getChildAt(paddingChildIndex).setPadding(0, 0, 0, convertDpToPx(50));
        hideViews(binding.gridLayoutCardInfo, hiddenViewIndices);
        if (materials.getTypeMaterials().equals("Канекалон")) {
            binding.typeKanekalonMaterialInfo.setText(materials.getTypeKanekalon());
            binding.manufactureKanekalonInfo.setText(materials.getManufacturer());
        } else if (materials.getTypeMaterials().equals("Кудри")) {
            binding.typeCurlsInfo.setText(materials.getTypeCurls());
            binding.lengthInfo.setText(String.valueOf(materials.getLength()));
        }
    }

    /**
     * Hides views in the grid layout based on the specified indices.
     *
     * @param gridLayout The grid layout containing the views to hide
     * @param indices    The indices of views to hide
     */
    private void hideViews(GridLayout gridLayout, int... indices) {
        for (int index : indices) {
            gridLayout.getChildAt(index).setVisibility(View.GONE);
        }
    }

    /**
     * Hides specific views in the grid layout and sets padding for a particular child view.
     *
     * @param hiddenViewIndices Array of indices of views to hide
     */
    private void hideMaterialInfo(int[] hiddenViewIndices) {
        hideViews(binding.gridLayoutCardInfo, hiddenViewIndices);
        binding.gridLayoutCardInfo.getChildAt(4).setPadding(0, 0, 0, convertDpToPx(50));
    }

    /**
     * Converts density-independent pixels (dp) to pixels (px).
     *
     * @param dp The value in dp to convert
     * @return The converted value in pixels (px)
     */
    private int convertDpToPx(int dp) {
        float scale = getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    /**
     * Initializes the menu by providing menu options for editing and deleting materials.
     * <p>
     * Implements the MenuProvider interface to inflate menu items and handle menu item selection.
     */
    private void initMenu() {
        MenuProvider provider = new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.menu_materials_card, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                if (R.id.menu_edit_material == menuItem.getItemId()) {
                    editMaterial();
                    return true;
                }
                if (R.id.menu_delete_material == menuItem.getItemId()) {
                    openConfirmationDialog();
                    return true;
                }
                return false;
            }
        };
        requireActivity().addMenuProvider(provider, getViewLifecycleOwner());
    }

    /**
     * Opens the EditCardMaterialDialog for editing the material information.
     */
    private void editMaterial() {
        EditCardMaterialDialog dialog = new EditCardMaterialDialog();
        dialog.setArguments(initBundleClient());
        dialog.show(getParentFragmentManager(), "DialogEditMaterial");
    }

    /**
     * Opens a confirmation dialog for deleting the material.
     */
    private void openConfirmationDialog() {
        ConfirmationDialog dialog = new ConfirmationDialog(
                getContext(),
                "Подтверждение",
                "Вы уверены, что хотите удалить этот материал?",
                (dialogInterface, which) -> {
                    if (which == DialogInterface.BUTTON_POSITIVE) {
                        deleteMaterial();
                    } else if (which == DialogInterface.BUTTON_NEGATIVE) {
                        dialogInterface.dismiss();
                    }
                });

        dialog.show();
    }

    /**
     * Deletes the current material and navigates back to the material list.
     */
    private void deleteMaterial() {
        viewModel.delete(material);
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
        navController.popBackStack(R.id.navigation_material, false);
    }

    /**
     * Initializes a Bundle with the current material object.
     *
     * @return The Bundle containing the material object
     */
    private Bundle initBundleClient() {
        Bundle arg = new Bundle();
        arg.putParcelable("materials", material);
        return arg;
    }
}