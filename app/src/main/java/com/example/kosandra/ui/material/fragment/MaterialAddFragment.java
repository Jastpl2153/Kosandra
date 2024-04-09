package com.example.kosandra.ui.material.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.example.kosandra.R;
import com.example.kosandra.databinding.FragmentMaterialAddBinding;
import com.example.kosandra.entity.Materials;
import com.example.kosandra.ui.general_logic.EmptyFields;
import com.example.kosandra.ui.general_logic.GalleryHandlerInterface;
import com.example.kosandra.view_model.MaterialsViewModel;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class MaterialAddFragment extends Fragment implements GalleryHandlerInterface, EmptyFields {
    private FragmentMaterialAddBinding binding;
    private ActivityResultLauncher<String> getContentLauncher;
    private MaterialsViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMaterialAddBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.newMaterialAddImage.setOnClickListener(v -> openGallery(getContentLauncher));
        setupGalleryResult(launcher -> getContentLauncher = launcher, getContext(), this, binding.newMaterialAddImage);
        initSpinner(binding.spinnerAddTypeMaterial, R.array.type_material);
        setOnClickListenerSpinnerType();
        initMenu();
    }

    private void setOnClickListenerSpinnerType(){
        binding.spinnerAddTypeMaterial.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setFragmentSpinnerType();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void setFragmentSpinnerType() {
        switch (binding.spinnerAddTypeMaterial.getSelectedItem().toString()){
            case "Канекалон":
                binding.spinnerAddKanekalonCurls.setVisibility(View.VISIBLE);
                binding.spinnerAddManufactureLength.setVisibility(View.VISIBLE);
                initSpinner(binding.spinnerAddKanekalonCurls, R.array.type_kanekalon_type_material);
                initSpinner(binding.spinnerAddManufactureLength, R.array.type_manufacture_material);
                break;
            case "Кудри":
                binding.spinnerAddKanekalonCurls.setVisibility(View.VISIBLE);
                binding.spinnerAddManufactureLength.setVisibility(View.VISIBLE);
                initSpinner(binding.spinnerAddKanekalonCurls, R.array.type_curls_material);
                initSpinner(binding.spinnerAddManufactureLength, R.array.type_length_material);
                break;
            case "Термоволокно":
                binding.spinnerAddKanekalonCurls.setVisibility(View.GONE);
                binding.spinnerAddManufactureLength.setVisibility(View.GONE);
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

    private void initMenu() {
        MenuProvider menuProvider = new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.menu_save, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.menu_save) {
                    saveMaterial();
                    return true;
                }
                return false;
            }
        };

        requireActivity().addMenuProvider(menuProvider, getViewLifecycleOwner());
    }

    private void saveMaterial() {
        viewModel = new ViewModelProvider(requireActivity()).get(MaterialsViewModel.class);
        Executors.newSingleThreadExecutor().execute(()->{
            if (validateEmptyFields() && validateCodeMaterial()) {
                try {
                    viewModel.insert(initMaterial());
                    NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
                    navController.popBackStack();
                } catch (Exception e){
                    e.printStackTrace();
                }
            } else {
                handleEmptyFields();
            }
        });
    }

    private Materials initMaterial(){
        Materials material = new Materials(
                binding.spinnerAddTypeMaterial.getSelectedItem().toString(),
                binding.addColorMaterial.getText().toString(),
                binding.addCodeMaterial.getText().toString(),
                getImageGallery(binding.newMaterialAddImage.getDrawable(), getResources()),
                Integer.parseInt(binding.addCountMaterial.getText().toString()),
                Integer.parseInt(binding.addPriceMaterial.getText().toString()),
                null,
                null,
                null,
                null,
                0);

        return initKanekalonOrCurls(material);
    }

    private Materials initKanekalonOrCurls(Materials material){
        switch (binding.spinnerAddTypeMaterial.getSelectedItem().toString()){
            case "Канекалон":
                material.setTypeKanekalon(binding.spinnerAddKanekalonCurls.getSelectedItem().toString());
                material.setManufacturer(binding.spinnerAddManufactureLength.getSelectedItem().toString());
                break;
            case "Кудри":
                material.setTypeCurls(binding.spinnerAddKanekalonCurls.getSelectedItem().toString());
                material.setLength(Integer.parseInt(binding.spinnerAddManufactureLength.getSelectedItem().toString()));
                break;
        }
        return material;
    }

    private boolean validateCodeMaterial() {
        for (String code : viewModel.getAllMaterialsCode()) {
            if (code.equals(binding.addCodeMaterial.getText().toString())) {
                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(requireContext(), "Такой код уже существует!", Toast.LENGTH_SHORT).show();
                    animateEmptyField(binding.addCodeMaterial, requireContext());
                    binding.addCodeMaterial.setTextColor(ContextCompat.getColor(requireContext(), R.color.red));
                });
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean validateEmptyFields() {
        return !binding.addColorMaterial.getText().toString().isEmpty() &&
                !binding.addCodeMaterial.getText().toString().isEmpty() &&
                !binding.addCountMaterial.getText().toString().isEmpty() &&
                !binding.addPriceMaterial.getText().toString().isEmpty();
    }

    @Override
    public void handleEmptyFields() {
        isEmpty(binding.addColorMaterial);
        isEmpty(binding.addCodeMaterial);
        isEmpty(binding.addCountMaterial);
        isEmpty(binding.addPriceMaterial);
    }
}