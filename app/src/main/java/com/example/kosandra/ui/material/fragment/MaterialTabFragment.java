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

/**
 * A Fragment class representing a tab for displaying materials.
 * <p>
 * Implements RvItemClickListener for handling item clicks.
 */
public class MaterialTabFragment extends Fragment implements RvItemClickListener<Materials> {
    private FragmentMaterialsTabBinding binding;
    private MaterialsViewModel viewModel;
    private AdapterRvMaterialsMain adapter;
    private int positionSelection = -1;
    private View prevItemLayout = null;
    private View prevButDelete = null;
    private String typeMaterial;
    private List<Materials> allMaterialFilter = new ArrayList<>();

    /**
     * Static method to create a new instance of MaterialTabFragment with a specified typeMaterial.
     *
     * @param typeMaterial The type of material to display
     * @return A new instance of MaterialTabFragment
     */
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

    /**
     * Initializes the RecyclerView adapter and sets it to the RecyclerView.
     */
    private void initRecyclerView() {
        adapter = new AdapterRvMaterialsMain(this);
        binding.rvMaterials.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvMaterials.setAdapter(adapter);
    }

    /**
     * Observes the list of materials from the ViewModel and updates the RecyclerView adapter.
     */
    private void observeListMaterial() {
        viewModel = new ViewModelProvider(requireActivity()).get(MaterialsViewModel.class);
        viewModel.getMaterialsList(typeMaterial).observe(getViewLifecycleOwner(), materials -> {
            if (materials != null) {
                adapter.setMaterials(materials);
                allMaterialFilter.addAll(materials);
            }
        });
    }

    /**
     * Filters the list of materials based on the given text.
     *
     * @param text The text used for filtering the materials list
     */
    protected void filter(String text) {
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

    /**
     * Handles the onClick event for an item in the materials list.
     * If the item is already selected, deselect it. If not, navigate to the material details screen.
     *
     * @param item_layout The layout of the clicked item
     * @param but_delete  The delete button in the item layout
     * @param materials   The materials associated with the clicked item
     */
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

    /**
     * Handles the onLongClick event for an item in the materials list.
     * Selects the item and animates selection. Deselects previously selected item if applicable.
     *
     * @param item_layout The layout of the clicked item
     * @param but_delete  The delete button in the item layout
     * @param materials   The materials associated with the clicked item
     */
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

    /**
     * Handles the onDeleteClick event for an item in the materials list.
     * Deletes the specified materials and cancels any ongoing animations on the item layout.
     *
     * @param item_layout The layout of the clicked item
     * @param but_delete  The delete button in the item layout
     * @param materials   The materials to be deleted
     */
    @Override
    public void onDeleteClick(View item_layout, View but_delete, Materials materials) {
        viewModel.delete(materials);
        AnimationHelper.cancelAnimation(item_layout, but_delete);
    }

    /**
     * Handles the onPhotoClick event for a material.
     * <p>
     * Displays a dialog with the full-size photo of the material if it is not the currently selected item.
     *
     * @param materials The materials associated with the clicked photo
     * @param image     The ImageView displaying the photo
     */
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

    /**
     * Retrieves all materials sorted by color in ascending order.
     * Updates the adapter and the filter list with the retrieved materials.
     */
    protected void getNameAllMaterialSortedByColorAscending() {
        viewModel.getAllMaterialSortedByColorAscending(typeMaterial).observe(getViewLifecycleOwner(), materials -> {
            if (materials != null) {
                adapter.setMaterials(materials);
                allMaterialFilter.addAll(materials);
            }
        });
    }

    /**
     * Retrieves all materials sorted by color in descending order.
     * Updates the adapter and the filter list with the retrieved materials.
     */
    protected void getNameAllMaterialSortedByColorDescending() {
        viewModel.getAllMaterialSortedByColorDescending(typeMaterial).observe(getViewLifecycleOwner(), materials -> {
            if (materials != null) {
                adapter.setMaterials(materials);
                allMaterialFilter.addAll(materials);
            }
        });
    }

    /**
     * Retrieves all materials sorted by count in ascending order.
     * Updates the adapter and the filter list with the retrieved materials.
     */
    protected void getNameAllMaterialSortedByCountAscending() {
        viewModel.getAllMaterialSortedByCountAscending(typeMaterial).observe(getViewLifecycleOwner(), materials -> {
            if (materials != null) {
                adapter.setMaterials(materials);
                allMaterialFilter.addAll(materials);
            }
        });
    }

    /**
     * Retrieves all materials sorted by count in descending order.
     * Updates the adapter and the filter list with the retrieved materials.
     */
    protected void getNameAllMaterialSortedByCountDescending() {
        viewModel.getAllMaterialSortedByCountDescending(typeMaterial).observe(getViewLifecycleOwner(), materials -> {
            if (materials != null) {
                adapter.setMaterials(materials);
                allMaterialFilter.addAll(materials);
            }
        });
    }

    /**
     * Retrieves all materials sorted by rating in ascending order.
     * Updates the adapter and the filter list with the retrieved materials.
     */
    protected void getAllMaterialSortedByRatingAscending() {
        viewModel.getAllMaterialSortedByRatingAscending(typeMaterial).observe(getViewLifecycleOwner(), materials -> {
            if (materials != null) {
                adapter.setMaterials(materials);
                allMaterialFilter.addAll(materials);
            }
        });
    }

    /**
     * Retrieves all materials sorted by rating in descending order.
     * Updates the adapter and the filter list with the retrieved materials.
     */
    protected void getAllMaterialSortedByRatingDescending() {
        viewModel.getAllMaterialSortedByRatingDescending(typeMaterial).observe(getViewLifecycleOwner(), materials -> {
            if (materials != null) {
                adapter.setMaterials(materials);
                allMaterialFilter.addAll(materials);
            }
        });
    }
}
