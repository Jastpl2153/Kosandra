package com.example.kosandra.ui.material.dialog;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.kosandra.R;
import com.example.kosandra.databinding.DialogMaterialsEditCardBinding;
import com.example.kosandra.entity.HairstyleVisit;
import com.example.kosandra.entity.Materials;
import com.example.kosandra.ui.general_logic.EmptyFields;
import com.example.kosandra.ui.general_logic.GalleryHandlerInterface;
import com.example.kosandra.view_model.HairstyleVisitViewModel;
import com.example.kosandra.view_model.MaterialsViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Arrays;

public class EditCardMaterialDialog extends BottomSheetDialogFragment implements GalleryHandlerInterface, EmptyFields {
    private DialogMaterialsEditCardBinding binding;
    private Materials material;
    private ActivityResultLauncher<String> getContentLauncher;
    private String prevCode;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogMaterialsEditCardBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getMaterial();
        initSpinners();
        initFields();
        setPrevCode();
        binding.editPhotoMaterial.setOnClickListener(v -> openGallery(getContentLauncher));
        setupGalleryResult(launcher -> getContentLauncher = launcher, getContext(), this, binding.editPhotoMaterial);
        binding.saveMaterial.setOnClickListener(v -> saveMaterial());
    }

    private void getMaterial() {
        material = getArguments() != null ? getArguments().getParcelable("materials") : null;
    }

    private void setPrevCode(){
        prevCode = binding.editCodeMaterial.getText().toString();
    }

    private void initSpinners() {
        switch (material.getTypeMaterials()) {
            case "Канекалон":
                initSpinner(binding.editSpinnerCurlsKanekalon, R.array.type_kanekalon_type_material);
                initSpinner(binding.editSpinnerManufactureLength, R.array.type_manufacture_material);
                break;
            case "Кудри":
                initSpinner(binding.editSpinnerCurlsKanekalon, R.array.type_curls_material);
                initSpinner(binding.editSpinnerManufactureLength, R.array.type_length_material);
                break;
            case "Термоволокно":
                binding.editSpinnerCurlsKanekalon.setVisibility(View.GONE);
                binding.editSpinnerManufactureLength.setVisibility(View.GONE);
                break;
        }
    }

    private void initSpinner(Spinner spinner, int arrayId) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),
                arrayId,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void initFields() {
        Bitmap bitmap = BitmapFactory.decodeByteArray(material.getPhoto(), 0, material.getPhoto().length);
        Drawable drawable = new BitmapDrawable(getResources(), bitmap);
        binding.editPhotoMaterial.setImageDrawable(drawable);

        binding.editTypeMaterial.setText(material.getTypeMaterials());
        binding.editColorMaterial.setText(material.getColorMaterial());
        binding.editCodeMaterial.setText(material.getCodeMaterial());
        binding.editCountMaterial.setText(String.valueOf(material.getCount()));
        binding.editPriceMaterial.setText(String.valueOf(material.getCost()));

        setSpinnerSelection(binding.editSpinnerCurlsKanekalon, material.getTypeKanekalon(), R.array.type_kanekalon_type_material);
        setSpinnerSelection(binding.editSpinnerManufactureLength, material.getManufacturer(), R.array.type_manufacture_material);
    }

    private void setSpinnerSelection(Spinner spinner, String value, int arrayId) {
        String[] array = getResources().getStringArray(arrayId);
        int index = Arrays.asList(array).indexOf(value);
        if (index != -1) {
            spinner.setSelection(index);
        }
    }

    private void saveMaterial() {
        if (validateEmptyFields()) {
            setMaterial();
            MaterialsViewModel viewModel = new ViewModelProvider(requireActivity()).get(MaterialsViewModel.class);
            updateHairstyle();
            viewModel.update(material);
            this.dismiss();
        } else {
            handleEmptyFields();
        }
    }

    private void updateHairstyle(){
        HairstyleVisitViewModel hairstyleVisitViewModel = new ViewModelProvider(requireActivity()).get(HairstyleVisitViewModel.class);
        hairstyleVisitViewModel.getAllHairstyleVisit().observe(getViewLifecycleOwner(), hairstyleVisits -> {
            for (HairstyleVisit visit : hairstyleVisits){
                boolean update = false;
                for (int i = 0; i < visit.getCodeMaterial().length; i++) {
                    if (prevCode.equals(visit.getCodeMaterial()[i])){
                        visit.getCodeMaterial()[i] = binding.editCodeMaterial.getText().toString();
                        update = true;
                    }
                }
                if (update){
                    hairstyleVisitViewModel.update(visit);
                }
            }
        });
    }

    private void setMaterial() {
        switch (binding.editTypeMaterial.getText().toString()) {
            case "Канекалон": {
                setKanekalon();
                break;
            }
            case "Кудри": {
                setCurls();
                break;
            }
            case "Термоволокно": {
                setThermalFiber();
                break;
            }
        }
    }

    private void setKanekalon(){
        material.setPhoto(getImageGallery(binding.editPhotoMaterial.getDrawable(), getResources()));
        material.setColorMaterial(binding.editColorMaterial.getText().toString());
        material.setCodeMaterial(binding.editCodeMaterial.getText().toString());
        material.setCount(Integer.parseInt(binding.editCountMaterial.getText().toString()));
        material.setCost(Integer.parseInt(binding.editPriceMaterial.getText().toString()));
        material.setTypeKanekalon(binding.editSpinnerCurlsKanekalon.getSelectedItem().toString());
        material.setManufacturer(binding.editSpinnerManufactureLength.getSelectedItem().toString());
    }

    private void setCurls(){
        material.setPhoto(getImageGallery(binding.editPhotoMaterial.getDrawable(), getResources()));
        material.setColorMaterial(binding.editColorMaterial.getText().toString());
        material.setCodeMaterial(binding.editCodeMaterial.getText().toString());
        material.setCount(Integer.parseInt(binding.editCountMaterial.getText().toString()));
        material.setCost(Integer.parseInt(binding.editPriceMaterial.getText().toString()));
        material.setTypeCurls(binding.editSpinnerCurlsKanekalon.getSelectedItem().toString());
        material.setLength(Integer.parseInt(binding.editSpinnerManufactureLength.getSelectedItem().toString()));
    }

    private void setThermalFiber(){
        material.setPhoto(getImageGallery(binding.editPhotoMaterial.getDrawable(), getResources()));
        material.setColorMaterial(binding.editColorMaterial.getText().toString());
        material.setCodeMaterial(binding.editCodeMaterial.getText().toString());
        material.setCount(Integer.parseInt(binding.editCountMaterial.getText().toString()));
        material.setCost(Integer.parseInt(binding.editPriceMaterial.getText().toString()));
    }

    @Override
    public boolean validateEmptyFields() {
        return !binding.editColorMaterial.getText().toString().isEmpty() &&
                !binding.editCodeMaterial.getText().toString().isEmpty() &&
                !binding.editCountMaterial.getText().toString().isEmpty() &&
                !binding.editPriceMaterial.getText().toString().isEmpty();
    }

    @Override
    public void handleEmptyFields() {
        isEmpty(binding.editColorMaterial);
        isEmpty(binding.editCodeMaterial);
        isEmpty(binding.editCountMaterial);
        isEmpty(binding.editPriceMaterial);
    }
}
