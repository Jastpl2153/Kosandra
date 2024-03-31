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

    private void getMaterial(){
        material = getArguments() != null ? getArguments().getParcelable("materials") : null;
    }

    private void observeMaterial(){
        if (material != null) {
            viewModel = new ViewModelProvider(this).get(MaterialsViewModel.class);
            viewModel.getMaterial(material.getId()).observe(getViewLifecycleOwner(), this::initSetFields);
        }
    }

    private void initSetFields(Materials materials) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(materials.getPhoto(), 0, materials.getPhoto().length);
        Drawable drawable =  new BitmapDrawable(getResources(), bitmap);
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

    private void hideViews(GridLayout gridLayout, int... indices) {
        for (int index : indices) {
            gridLayout.getChildAt(index).setVisibility(View.GONE);
        }
    }

    private void hideMaterialInfo(int[] hiddenViewIndices) {
        hideViews(binding.gridLayoutCardInfo, hiddenViewIndices);
        binding.gridLayoutCardInfo.getChildAt(4).setPadding(0, 0, 0, convertDpToPx(50));
    }

    private int convertDpToPx(int dp) {
        float scale = getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    private void initMenu(){
        MenuProvider provider = new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.menu_materials_card, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                if (R.id.menu_edit_material == menuItem.getItemId()){
                    editMaterial();
                    return true;
                }
                if (R.id.menu_delete_material == menuItem.getItemId()){
                    openConfirmationDialog();
                    return true;
                }
                return false;
            }
        };
        requireActivity().addMenuProvider(provider, getViewLifecycleOwner());
    }

    private void editMaterial() {
        EditCardMaterialDialog dialog = new EditCardMaterialDialog();
        dialog.setArguments(initBundleClient());
        dialog.show(getParentFragmentManager(), "DialogEditMaterial");
    }

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

    private void deleteMaterial() {
        viewModel.delete(material);
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
        navController.popBackStack(R.id.navigation_material, false);
    }

    private Bundle initBundleClient() {
        Bundle arg = new Bundle();
        arg.putParcelable("materials", material);
        return arg;
    }
}