package com.example.kosandra.ui.material.fragment;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.example.kosandra.R;
import com.example.kosandra.databinding.FragmentMaterialsTabBinding;
import com.example.kosandra.entity.Materials;
import com.example.kosandra.ui.animation.AnimationHelper;
import com.example.kosandra.ui.general_logic.RvItemClickListener;
import com.example.kosandra.ui.material.adapter.AdapterRvMaterialsMain;
import com.example.kosandra.view_model.MaterialsViewModel;

import java.util.ArrayList;
import java.util.List;

public class MaterialTabFragment extends Fragment implements RvItemClickListener<Materials> {
    private FragmentMaterialsTabBinding binding;
    private MaterialsViewModel viewModel;
    private AdapterRvMaterialsMain adapter;
    private int positionSelection = -1;
    private View prevItemLayout = null;
    private View prevButDelete = null;
    private String typeMaterial;
    private List<Materials> allMaterialFilter = new ArrayList<>();

    public static MaterialTabFragment newInstance(String typeMaterial) {
        MaterialTabFragment fragment = new MaterialTabFragment();
        Bundle args = new Bundle();
        args.putString("typeMaterial", typeMaterial);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMaterialsTabBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        typeMaterial = getArguments().getString("typeMaterial");
        initRecyclerView();
        observeListMaterial();
    }

    private void initRecyclerView(){
        adapter = new AdapterRvMaterialsMain(this);
        binding.rvMaterials.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvMaterials.setAdapter(adapter);
    }

    private void observeListMaterial(){
        viewModel = new ViewModelProvider(requireActivity()).get(MaterialsViewModel.class);
        viewModel.getMaterialsList(typeMaterial).observe(getViewLifecycleOwner(), materials -> {
                if (materials != null) {
                    adapter.setMaterials(materials);
                    allMaterialFilter.addAll(materials);
                }
            });
    }

    protected void filter(String text){
        if (adapter != null) {
            List<Materials> filter = new ArrayList<>();
            for (Materials materials : allMaterialFilter) {
                if (materials.getColorMaterial().toLowerCase().contains(text.toLowerCase())
                || materials.getCodeMaterial().toLowerCase().contains(text.toLowerCase())) {
                    filter.add(materials);
                }
            }
            adapter.setMaterials(filter);
        }
    }

    @Override
    public void onClick(View item_layout, View but_delete, Materials materials) {
        if (positionSelection == materials.getId()) {
            AnimationHelper.animateItemDeselected(item_layout, but_delete);
            positionSelection = -1;
        } else {
            Bundle args = new Bundle();
            args.putParcelable("materials", materials);
            Navigation.findNavController(item_layout)
                    .navigate(R.id.open_material_card_navigation, args);
        }
    }

    @Override
    public void onLongClick(View item_layout, View but_delete, Materials materials) {
        if (positionSelection == -1 || positionSelection == materials.getId()) {
            positionSelection = materials.getId();
            AnimationHelper.animateItemSelected(item_layout, but_delete);
            prevItemLayout = item_layout;
            prevButDelete = but_delete;
        } else if (prevItemLayout != null && prevButDelete != null) {
            positionSelection = materials.getId();
            AnimationHelper.animateItemSelected(item_layout, but_delete);
            AnimationHelper.animateItemDeselected(prevItemLayout, prevButDelete);
            prevItemLayout = item_layout;
            prevButDelete = but_delete;
        }
    }

    @Override
    public void onDeleteClick(View item_layout, View but_delete, Materials materials) {
        viewModel.delete(materials);
        AnimationHelper.cancelAnimation(item_layout, but_delete);
    }

    @Override
    public void onPhotoClick(Materials materials, View image) {
        if (materials != null && positionSelection != materials.getId()) {
            Dialog dialog = new Dialog(image.getContext(), android.R.style.Theme_Material_Dialog_NoActionBar_MinWidth);
            dialog.setContentView(R.layout.dialog_client_photo_card);

            SubsamplingScaleImageView imageView = dialog.findViewById(R.id.photo);

            Bitmap bitmap = BitmapFactory.decodeByteArray(materials.getPhoto(), 0, materials.getPhoto().length);
            imageView.setImage(ImageSource.bitmap(bitmap));

            dialog.show();
        }
    }

    protected void getNameAllMaterialSortedByColorAscending(){
        viewModel.getAllMaterialSortedByColorAscending(typeMaterial).observe(getViewLifecycleOwner(), materials -> {
            if (materials != null) {
                adapter.setMaterials(materials);
                allMaterialFilter.addAll(materials);
            }
        });
    }

    protected void getNameAllMaterialSortedByColorDescending(){
        viewModel.getAllMaterialSortedByColorDescending(typeMaterial).observe(getViewLifecycleOwner(), materials -> {
            if (materials != null) {
                adapter.setMaterials(materials);
                allMaterialFilter.addAll(materials);
            }
        });
    }

    protected void getNameAllMaterialSortedByCountAscending(){
        viewModel.getAllMaterialSortedByCountAscending(typeMaterial).observe(getViewLifecycleOwner(), materials -> {
            if (materials != null) {
                adapter.setMaterials(materials);
                allMaterialFilter.addAll(materials);
            }
        });
    }

    protected void getNameAllMaterialSortedByCountDescending(){
        viewModel.getAllMaterialSortedByCountDescending(typeMaterial).observe(getViewLifecycleOwner(), materials -> {
            if (materials != null) {
                adapter.setMaterials(materials);
                allMaterialFilter.addAll(materials);
            }
        });
    }

    protected void getAllMaterialSortedByRatingAscending(){
        viewModel.getAllMaterialSortedByRatingAscending(typeMaterial).observe(getViewLifecycleOwner(), materials -> {
            if (materials != null) {
                adapter.setMaterials(materials);
                allMaterialFilter.addAll(materials);
            }
        });
    }

    protected void getAllMaterialSortedByRatingDescending(){
        viewModel.getAllMaterialSortedByRatingDescending(typeMaterial).observe(getViewLifecycleOwner(), materials -> {
            if (materials != null) {
                adapter.setMaterials(materials);
                allMaterialFilter.addAll(materials);
            }
        });
    }
}
